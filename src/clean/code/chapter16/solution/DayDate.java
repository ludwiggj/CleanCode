package clean.code.chapter16.solution;

/* ========================================================================
 * JCommon : a free general purpose class library for the Java(tm) platform
 * ========================================================================
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
.
*/

import java.io.Serializable;
import java.util.*;

/**
 * An abstract class that represents immutable dates with a precision of
 * one day.  The implementation will map each date to an integer that
 * represents an ordinal number of days from some fixed origin.
 * <p/>
 * Why not just use java.util.Date?  We will, when it makes sense.  At times,
 * java.util.Date can be *too* precise - it represents an instant in time,
 * accurate to 1/1000th of a second (with the date itself depending on the
 * time-zone).  Sometimes we just want to represent a particular day (e.g. 21
 * January 2015) without concerning ourselves about the time of day, or the
 * time-zone, or anything else.  That's what we've defined DayDate for.
 * <p/>
 * Use DayDateFactory.makeDate to create an instance.
 *
 * @author David Gilbert
 * @author Robert C. Martin did a lot of refactoring.
 */

public abstract class DayDate implements Comparable, Serializable {
  public abstract int getOrdinalDay();

  public abstract int getYear();

  public abstract Month getMonth();

  public abstract int getDayOfMonth();

  protected abstract Day getDayOfWeekForOrdinalZero();

  public DayDate plusDays(int days) {
    return DayDateFactory.makeDate(getOrdinalDay() + days);
  }

  public DayDate plusMonths(int months) {
    int thisMonthAsOrdinal = getMonth().toInt() - Month.JANUARY.toInt();
    int thisMonthAndYearAsOrdinal = 12 * getYear() + thisMonthAsOrdinal;
    int resultMonthAndYearAsOrdinal = thisMonthAndYearAsOrdinal + months;
    int resultYear = resultMonthAndYearAsOrdinal / 12;
    int resultMonthAsOrdinal = resultMonthAndYearAsOrdinal % 12 + Month.JANUARY.toInt();
    Month resultMonth = Month.fromInt(resultMonthAsOrdinal);
    int resultDay = correctLastDayOfMonth(getDayOfMonth(), resultMonth, resultYear);
    return DayDateFactory.makeDate(resultDay, resultMonth, resultYear);
  }

  public DayDate plusYears(int years) {
    int resultYear = getYear() + years;
    int resultDay = correctLastDayOfMonth(getDayOfMonth(), getMonth(), resultYear);
    return DayDateFactory.makeDate(resultDay, getMonth(), resultYear);
  }

  private int correctLastDayOfMonth(int day, Month month, int year) {
    int lastDayOfMonth = DateUtil.lastDayOfMonth(month, year);
    if (day > lastDayOfMonth)
      day = lastDayOfMonth;
    return day;
  }

  public DayDate getPreviousDayOfWeek(Day targetDayOfWeek) {
    int offsetToTarget = targetDayOfWeek.toInt() - getDayOfWeek().toInt();
    if (offsetToTarget >= 0)
      offsetToTarget -= 7;
    return plusDays(offsetToTarget);
  }

  public DayDate getFollowingDayOfWeek(Day targetDayOfWeek) {
    int offsetToTarget = targetDayOfWeek.toInt() - getDayOfWeek().toInt();
    if (offsetToTarget <= 0)
      offsetToTarget += 7;
    return plusDays(offsetToTarget);
  }

  public DayDate getNearestDayOfWeek(Day targetDayOfWeek) {
    int offsetToThisWeeksTarget = targetDayOfWeek.toInt() - getDayOfWeek().toInt();
    int offsetToFutureTarget = (offsetToThisWeeksTarget + 7) % 7;
    int offsetToPreviousTarget = offsetToFutureTarget - 7;

    if (offsetToFutureTarget > 3)
      return plusDays(offsetToPreviousTarget);
    else
      return plusDays(offsetToFutureTarget);
  }

  public DayDate getEndOfMonth() {
    Month month = getMonth();
    int year = getYear();
    int lastDay = DateUtil.lastDayOfMonth(month, year);
    return DayDateFactory.makeDate(lastDay, month, year);
  }

  public Date toDate() {
    final Calendar calendar = Calendar.getInstance();
    int ordinalMonth = getMonth().toInt() - Month.JANUARY.toInt();
    calendar.set(getYear(), ordinalMonth, getDayOfMonth(), 0, 0, 0);
    return calendar.getTime();
  }

  public String toString() {
    return String.format("%02d-%s-%d", getDayOfMonth(), getMonth(), getYear());
  }

  public Day getDayOfWeek() {
    Day startingDay = getDayOfWeekForOrdinalZero();
    int startingOffset = startingDay.toInt() - Day.SUNDAY.toInt();
    int ordinalOfDayOfWeek = (getOrdinalDay() + startingOffset) % 7;
    return Day.fromInt(ordinalOfDayOfWeek + Day.SUNDAY.toInt());
  }

  public int daysSince(DayDate date) {
    return getOrdinalDay() - date.getOrdinalDay();
  }

  public boolean isOn(DayDate other) {
    return getOrdinalDay() == other.getOrdinalDay();
  }

  public boolean isBefore(DayDate other) {
    return getOrdinalDay() < other.getOrdinalDay();
  }

  public boolean isOnOrBefore(DayDate other) {
    return getOrdinalDay() <= other.getOrdinalDay();
  }

  public boolean isAfter(DayDate other) {
    return getOrdinalDay() > other.getOrdinalDay();
  }

  public boolean isOnOrAfter(DayDate other) {
    return getOrdinalDay() >= other.getOrdinalDay();
  }

  public boolean isInRange(DayDate d1, DayDate d2) {
    return isInRange(d1, d2, DateInterval.CLOSED);
  }

  public boolean isInRange(DayDate d1, DayDate d2, DateInterval interval) {
    int left = Math.min(d1.getOrdinalDay(), d2.getOrdinalDay());
    int right = Math.max(d1.getOrdinalDay(), d2.getOrdinalDay());
    return interval.isIn(getOrdinalDay(), left, right);
  }
}