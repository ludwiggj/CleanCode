package clean.code.chapter16.solution;

import java.text.DateFormatSymbols;

public class DateUtil {
  private static DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();

  public static String[] getMonthNames() {
    return dateFormatSymbols.getMonths();
  }

  public static boolean isLeapYear(int year) {
    boolean fourth = year % 4 == 0;
    boolean hundredth = year % 100 == 0;
    boolean fourHundredth = year % 400 == 0;
    return fourth && (!hundredth || fourHundredth);
  }

  public static int lastDayOfMonth(Month month, int year) {
    if (month == Month.FEBRUARY && isLeapYear(year))
      return month.lastDay() + 1;
    else
      return month.lastDay();
  }

  public static int leapYearCount(int year) {
    int leap4 = (year - 1896) / 4;
    int leap100 = (year - 1800) / 100;
    int leap400 = (year - 1600) / 400;
    return leap4 - leap100 + leap400;
  }
}