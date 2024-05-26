package ch.malbun.kalender.ui.time;

import ch.malbun.kalender.ui.enums.Months;

import java.io.Serial;
import java.io.Serializable;

public class Year implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  public Month january;
  public Month february;
  public Month april;
  public Month march;
  public Month may;
  public Month june;
  public Month july;
  public Month august;
  public Month september;
  public Month october;
  public Month november;
  public Month december;
  public int year;

  public Year(int year) {
    this.year = year;

    january = new Month(Months.JANUARY, this);
    february = new Month(Months.FEBRUARY, this);
    march = new Month(Months.MARCH, this);
    april = new Month(Months.APRIL, this);
    may = new Month(Months.MAY, this);
    june = new Month(Months.JUNE, this);
    july = new Month(Months.JULY, this);
    august = new Month(Months.AUGUST, this);
    september = new Month(Months.SEPTEMBER, this);
    october = new Month(Months.OCTOBER, this);
    november = new Month(Months.NOVEMBER, this);
    december = new Month(Months.DECEMBER, this);
  }

  public static boolean isLeapYear(int year) {
    return year == 2024 || year == 2028 || year == 2032;
  }

  public boolean isLeapYear() {
    return isLeapYear(year);
  }

  public Month getMonth(Months months) {
    switch (months) {
      case JANUARY -> {
        return january;
      } case FEBRUARY -> {
        return february;
      } case MARCH -> {
        return march;
      } case APRIL -> {
        return april;
      } case MAY -> {
        return may;
      } case JUNE -> {
        return june;
      } case JULY -> {
        return july;
      } case AUGUST -> {
        return august;
      } case SEPTEMBER -> {
        return september;
      } case OCTOBER -> {
        return october;
      } case NOVEMBER -> {
        return november;
      } case DECEMBER -> {
        return december;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return String.valueOf(year);
  }

  public void reloadMonth(Months months) {
    getMonth(months).reload();
  }

  public void reloadAll() {
    for (Months months : Months.values()) reloadMonth(months);
  }

}