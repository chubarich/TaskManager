package com.danielkashin.taskorganiser.util;

import android.util.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DatetimeHelper {

  public static final String DATE_PATTERN = "yyyy-MM-dd";
  public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm";


  public static String getDatetimeFromTimestamp(Long timestamp) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN);

    return simpleDateFormat.format(new Date(timestamp * 1000));
  }

  public static boolean isGreaterThanNow(long timestamp, int neededHalfHourDifference) {
    long now = new Date().getTime() / 1000;

    return now + (neededHalfHourDifference * 30 * 60) < timestamp;
  }

  public static Long getTimestamp(int year, int month, int day, int hour, int minute) {
    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minute);
    calendar.getTime();

    return calendar.getTimeInMillis() / 1000;
  }



  public static String getUpYear(String date) {
    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]);
    int day = Integer.valueOf(date.split("-")[2]);

    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.YEAR, year + 1);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.getTime();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    String output = simpleDateFormat.format(calendar.getTime());
    return output;
  }

  public static String getDownYear(String date) {
    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]);
    int day = Integer.valueOf(date.split("-")[2]);

    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.YEAR, year - 1);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.getTime();


    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    String output = simpleDateFormat.format(calendar.getTime());
    return output;
  }

  public static String getUpMonth(String date) {
    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]);
    int day = Integer.valueOf(date.split("-")[2]);

    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.getTime();

    calendar.add(Calendar.MONTH, 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.getTime();


    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    String output = simpleDateFormat.format(calendar.getTime());
    return output;
  }

  public static String getDownMonth(String date) {
    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]);
    int day = Integer.valueOf(date.split("-")[2]);

    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.getTime();

    calendar.add(Calendar.MONTH, -1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.getTime();


    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    String output = simpleDateFormat.format(calendar.getTime());
    return output;
  }

  public static String getUpWeek(String date) {
    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]);
    int day = Integer.valueOf(date.split("-")[2]);

    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.getTime();

    calendar.add(Calendar.WEEK_OF_YEAR, 1);
    calendar.getTime();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    String output = simpleDateFormat.format(calendar.getTime());
    return output;
  }

  public static String getDownWeek(String date) {
    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]);
    int day = Integer.valueOf(date.split("-")[2]);

    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.getTime();

    calendar.add(Calendar.WEEK_OF_YEAR, -1);
    calendar.getTime();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    String output = simpleDateFormat.format(calendar.getTime());
    return output;
  }

  public static String getUpDay(String date) {
    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]);
    int day = Integer.valueOf(date.split("-")[2]);

    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.getTime();

    calendar.add(Calendar.DAY_OF_YEAR, 1);
    calendar.getTime();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    String output = simpleDateFormat.format(calendar.getTime());
    return output;
  }

  public static String getDownDay(String date) {
    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]);
    int day = Integer.valueOf(date.split("-")[2]);

    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.getTime();

    calendar.add(Calendar.DAY_OF_YEAR, -1);
    calendar.getTime();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    String output = simpleDateFormat.format(calendar.getTime());
    return output;
  }

  public static int getYear(String date) {
    return Integer.valueOf(date.split("-")[0]);
  }

  public static Month getMonth(String date) {
    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]);

    return new Month(year, month);
  }

  public static Day getDay(String date) {
    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]);
    int dayOfMonth = Integer.valueOf(date.split("-")[2]);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

    int dayOfWeek = dayOfWeekStartingSunday(calendar.get(Calendar.DAY_OF_WEEK));

    return new Day(dayOfWeek, dayOfMonth, month);
  }

  public static Pair<Day, Day> getFirstAndLastDaysOfWeek(String date) {
    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]);
    int dayOfMonth = Integer.valueOf(date.split("-")[2]);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    calendar.getTime();
    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeekStartingMonday(1));
    calendar.getTime();
    Day first = new Day(dayOfWeekStartingSunday(calendar.get(Calendar.DAY_OF_WEEK)),
        calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    calendar.getTime();
    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeekStartingMonday(7));
    calendar.getTime();
    Day last = new Day(dayOfWeekStartingSunday(calendar.get(Calendar.DAY_OF_WEEK)),
        calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);

    return new Pair<>(first, last);
  }



  public static ArrayList<String> getFirstDaysOfWeeksInMonth(String monthDate) {
    int year = Integer.valueOf(monthDate.split("-")[0]);
    int month = Integer.valueOf(monthDate.split("-")[1]);

    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    int countOfWeeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

    ArrayList<String> firstDaysOfWeeks = new ArrayList<>();
    for (int i = 1; i <= countOfWeeks; ++i) {
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.MONTH, month - 1);
      calendar.set(Calendar.WEEK_OF_MONTH, i);
      calendar.set(Calendar.DAY_OF_WEEK, dayOfWeekStartingMonday(1));
      firstDaysOfWeeks.add(simpleDateFormat.format(calendar.getTime()));
    }

    return firstDaysOfWeeks;
  }

  public static String getMonthDate(String monthDate) {
    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    int year = Integer.valueOf(monthDate.split("-")[0]);
    int month = Integer.valueOf(monthDate.split("-")[1]);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.getTime();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    String output = simpleDateFormat.format(calendar.getTime());
    return output;
  }

  public static ArrayList<String> getAllMonthsInYear(String yearDate) {
    int year = Integer.valueOf(yearDate.split("-")[0]);

    ArrayList<String> output = new ArrayList<>();
    for (int i = 1; i < 13; ++i) {
      output.add(year + "-" + String.format("%02d", i) + "-" + "01");
    }

    return output;
  }

  public static ArrayList<String> getAllDaysOfWeek(String dayDate) {
    int year = Integer.valueOf(dayDate.split("-")[0]);
    int month = Integer.valueOf(dayDate.split("-")[1]);
    int day = Integer.valueOf(dayDate.split("-")[2]);

    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
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


  public static long getCurrentTimestamp() {
    return Calendar.getInstance(new Locale("ru,ru")).getTimeInMillis() / 1000;
  }

  public static String getCurrentMonth() {
    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.DAY_OF_MONTH, 1);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    return simpleDateFormat.format(calendar.getTime());
  }

  public static String getCurrentWeek() {
    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeekStartingMonday(1));
    calendar.getTime();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    return simpleDateFormat.format(calendar.getTime());
  }

  public static String getWeekDate(String date) {
    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]);
    int dayOfMonth = Integer.valueOf(date.split("-")[2]);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    calendar.getTime();
    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeekStartingMonday(1));
    calendar.getTime();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    return simpleDateFormat.format(calendar.getTime());
  }

  public static String getCurrentDay() {
    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    return simpleDateFormat.format(calendar.getTime());
  }


  public static String getDayLabel(String[] months, String[] days, String dayDate) {
    Day day = DatetimeHelper.getDay(dayDate);
    String label = days[day.getDayOfWeek() - 1] + ", " + day.getDayOfMonth() + " "
        + months[day.getMonth() - 1];

    return label;
  }

  public static String getWeekLabel(String[] months, String weekDate) {
    Pair<Day, Day> firstAndLastDays = DatetimeHelper.getFirstAndLastDaysOfWeek(weekDate);
    String firstDayMonth = months[firstAndLastDays.first.getMonth() - 1];
    String lastDayMonth = months[firstAndLastDays.second.getMonth() - 1];

    String label = firstAndLastDays.first.getDayOfMonth()
        + (firstDayMonth.equals(lastDayMonth) ? "" : " " + firstDayMonth) + " - "
        + firstAndLastDays.second.getDayOfMonth() + " "
        + lastDayMonth;

    return label;
  }

  public static String getMonthLabel(String[] months, String monthDate) {
    Month month = DatetimeHelper.getMonth(monthDate);
    String label = months[month.getMonth() - 1] + ", " + month.getYear();

    return label;
  }

  public static String getYearLabel(String yearDate) {
    int year = DatetimeHelper.getYear(yearDate);
    return "" + year;
  }

  public static int getCurrentDayOfMonthNumber() {
    return Calendar.getInstance(new Locale("ru","ru")).get(Calendar.DAY_OF_MONTH);
  }

  public static int getCurrentMonthNumber() {
    return Calendar.getInstance(new Locale("ru","ru")).get(Calendar.MONTH);
  }

  public static int getCurrentYearNumber() {
    return Calendar.getInstance(new Locale("ru","ru")).get(Calendar.YEAR);
  }

  public static String getDate(int year, int month, int day) {
    Calendar calendar = Calendar.getInstance(new Locale("ru", "ru"));
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.getTime();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    simpleDateFormat.setTimeZone(calendar.getTimeZone());

    String output = simpleDateFormat.format(calendar.getTime());
    return output;
  }

  // ---------------------------------------- private ---------------------------------------------

  private static int dayOfWeekStartingMonday(int dayOfWeek) {
    return (dayOfWeek + 2) % 7 - 1;
  }

  public static int dayOfWeekStartingSunday(int dayOfWeek) {
    return (dayOfWeek + 5) % 7 + 1;
  }


  public static class Month {

    private final int year;

    private final int month;

    public Month(int year, int month) {
      this.year = year;
      this.month = month;
    }

    public int getMonth() {
      return month;
    }

    public int getYear() {
      return year;
    }
  }

  public static class Day {

    // [1..7]
    private final int dayOfWeek;

    // [1..31]
    private final int dayOfMonth;

    // [1..12]
    private final int month;

    public Day(int dayOfWeek, int dayOfMonth, int month) {
      this.dayOfWeek = dayOfWeek;
      this.dayOfMonth = dayOfMonth;
      this.month = month;
    }

    public int getDayOfWeek() {
      return dayOfWeek;
    }

    public int getDayOfMonth() {
      return dayOfMonth;
    }

    public int getMonth() {
      return month;
    }
  }

}
