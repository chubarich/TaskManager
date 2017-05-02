package com.danielkashin.taskorganiser.domain_layer.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class DateTimeHelper {

  // weeks id DB are represented by their first days
  public static ArrayList<YearDay> getFirstDaysOfWeeksInMonth(int year, int month) {
    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month);
    int countOfWeeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

    ArrayList<YearDay> firstDaysOfWeeks = new ArrayList<>();
    for (int i = 1; i <= countOfWeeks; ++i) {
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.MONTH, month);
      calendar.set(Calendar.WEEK_OF_MONTH, i);
      calendar.set(Calendar.DAY_OF_WEEK, dayOfWeekStartingMonday(1));
      firstDaysOfWeeks.add(new YearDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_YEAR)));
    }

    return firstDaysOfWeeks;
  }

  public static ArrayList<YearDay> getAllDaysOfWeek(YearDay dayOfWeek) {
    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    ArrayList<YearDay> daysOfWeek = new ArrayList<>();
    for (int i = 1; i <= 7; ++i) {
      calendar.set(Calendar.YEAR, dayOfWeek.getYear());
      calendar.set(Calendar.DAY_OF_YEAR, dayOfWeek.getDayOfYear());
      calendar.getTime();
      calendar.set(Calendar.DAY_OF_WEEK, dayOfWeekStartingMonday(i));

      daysOfWeek.add(new YearDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_YEAR)));
    }

    return daysOfWeek;
  }


  private static int dayOfWeekStartingMonday(int dayOfWeek) {
    return (dayOfWeek + 2) % 7 - 1;
  }

  public static int dayOfWeekStartingSunday(int dayOfWeek) {
    return (dayOfWeek + 5) % 7 + 1;
  }

  // ------------------------------------- inner types --------------------------------------------

  public static class Week {

    private YearDay firstDay;

  }

  public static class YearDay {

    private int year;

    private int dayOfYear;

    public YearDay(int year, int dayOfYear) {
      this.year = year;
      this.dayOfYear = dayOfYear;
    }

    public int getYear() {
      return year;
    }

    public int getDayOfYear() {
      return dayOfYear;
    }

  }


}
