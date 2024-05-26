package ch.malbun.kalender.ui.enums;

import ch.malbun.kalender.ui.exceptions.NoMonthsException;

public enum Months {
  JANUARY(1),
  FEBRUARY(2),
  MARCH(3),
  APRIL(4),
  MAY(5),
  JUNE(6),
  JULY(7),
  AUGUST(8),
  SEPTEMBER(9),
  OCTOBER(10),
  NOVEMBER(11),
  DECEMBER(12);

  private int numVal;

  Months(int val) {
    numVal = val;
  }

  public static Months byValue(int val) {
    for (Months months : Months.values()) {
      if (months.getVal() == val) {
        return months;
      }
    }
    throw new NoMonthsException("No Months for: " + val);
  }

  public int getVal() {
    return numVal;
  }

  @Override
  public String toString() {
    String val = String.valueOf(numVal);
    if (val.length() == 1) {
      return "0" + val;
    } else return val;
  }
}
