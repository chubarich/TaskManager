package com.danielkashin.taskorganiser.domain_layer.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class DatetimeHelper {

  public static final String PATTERN = "yyyy-MM-dd";


  // weeks id DB are represented by their first days
  public static ArrayList<String> getFirstDaysOfWeeksInMonth(String monthDate) {
    int year = Integer.valueOf(monthDate.split("-")[0]);
    int month = Integer.valueOf(monthDate.split("-")[1]);

    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    int countOfWeeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

    ArrayList<String> firstDaysOfWeeks = new ArrayList<>();
    for (int i = 1; i <= countOfWeeks; ++i) {
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.MONTH, month);
      calendar.set(Calendar.WEEK_OF_MONTH, i);
      calendar.set(Calendar.DAY_OF_WEEK, dayOfWeekStartingMonday(1));
      firstDaysOfWeeks.add(simpleDateFormat.format(calendar.getTime()));
    }

    return firstDaysOfWeeks;
  }

  public static ArrayList<String> getAllDaysOfWeek(String firstDayDate) {
    int year = Integer.valueOf(firstDayDate.split("-")[0]);
    int month = Integer.valueOf(firstDayDate.split("-")[1]);
    int day = Integer.valueOf(firstDayDate.split("-")[2]);

    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());


    ArrayList<String> daysOfWeek = new ArrayList<>();
    for (int i = 1; i <= 7; ++i) {
      String string;
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.MONTH, month - 1);
      calendar.set(Calendar.DAY_OF_MONTH, day);
      calendar.getTime();
      calendar.set(Calendar.DAY_OF_WEEK, dayOfWeekStartingMonday(i));
      calendar.getTime();

      daysOfWeek.add(simpleDateFormat.format(calendar.getTime()));
    }

    return daysOfWeek;
  }

  public static String getCurrentFirstDayOfWeek() {
    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeekStartingMonday(1));

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    return simpleDateFormat.format(calendar.getTime());
  }

  private static int dayOfWeekStartingMonday(int dayOfWeek) {
    return (dayOfWeek + 2) % 7 - 1;
  }

  public static int dayOfWeekStartingSunday(int dayOfWeek) {
    return (dayOfWeek + 5) % 7 + 1;
  }

}
