package clean.code.chapter16.original;

/* ========================================================================
ßary for the Java(tm) platform
ß=============================
ß
ß Limited and Contributors.
ß
ß/index.html
ß
ßstribute it and/or modify it
ßublic License as published by
ßn 2.1 of the License, or
ß
ß
ßt it will be useful, but
ßied warranty of MERCHANTABILITY
ße GNU Lesser General Public
ß
ß
ßLesser General Public
ßite to the Free Software
ß Floor, Boston, MA  02110-1301,
  * USA.
  *
  * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
  * in the United States and other countries.]
  *
  * -------------------
  * MonthConstants.java
  * -------------------
  * (C) Copyright 2002, 2003, by Object Refinery Limited.
  *
  * Original Author:  David Gilbert (for Object Refinery Limited);
  * Contributor(s):   -;
  *
  * $Id: MonthConstants.java,v 1.4 2005/11/16 15:58:40 taqua Exp $
  *
  * Changes
  * -------
  * 29-May-2002 : Version 1 (code moved from SerialDate class) (DG);
  *
  */

 /**
  * Useful constants for months.  Note that these are NOT equivalent to the
  * constants defined by java.util.Calendar (where JANUARY=0 and DECEMBER=11).
  * <P>
  * Used by the SerialDate and RegularTimePeriod classes.
  *
  * @author David Gilbert
  */
 public interface MonthConstants {

     /** Constant for January. */
     public static final int JANUARY = 1;

     /** Constant for February. */
     public static final int FEBRUARY = 2;

     /** Constant for March. */
     public static final int MARCH = 3;

     /** Constant for April. */
     public static final int APRIL = 4;

     /** Constant for May. */
     public static final int MAY = 5;

     /** Constant for June. */
     public static final int JUNE = 6;

     /** Constant for July. */
     public static final int JULY = 7;

     /** Constant for August. */
     public static final int AUGUST = 8;

     /** Constant for September. */
     public static final int SEPTEMBER = 9;

     /** Constant for October. */
     public static final int OCTOBER = 10;

     /** Constant for November. */
     public static final int NOVEMBER = 11;

     /** Constant for December. */
     public static final int DECEMBER = 12;

}