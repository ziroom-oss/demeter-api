package com.ziroom.tech.demeterapi.open.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * @author xuzeyu
 */
public class DateUtil {

    public static final DateTimeFormatter dateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter datePatternToMonth = DateTimeFormatter.ofPattern("yyyy-MM");

    public static String parseDateTime(LocalDateTime dateTime) {
        return dateTime.format(dateTimePattern);
    }

    public static String getNowDateTimeStrToSecond() {
        return LocalDateTime.now().format(dateTimePattern);
    }

    public static String getNowDateTimeStrToDay(LocalDate dateTime) {
        return dateTime.format(datePattern);
    }

    public static String parseDateTimeToDay(LocalDateTime dateTime){
        return dateTime.format(dateTimePattern);
    }

    public static LocalDateTime getNowDateTime() {
        return LocalDateTime.now();
    }

    public static String getNowDateTimeStrToMonth() {
        return LocalDateTime.now().format(datePatternToMonth);
    }

    public static String getNowDateTimeStrToDay() {
        return LocalDateTime.now().format(datePattern);
    }

    public static LocalDateTime getStartDateTimeOfCurrentMonth() {
        return LocalDateTime.now().minusDays(LocalDate.now().getDayOfMonth() - 1).withHour(0).withMinute(0).withSecond(0);
    }

    public static String getStartDateTimeOfCurrentMonthStr() {
        return getStartDateTimeOfCurrentMonth().format(dateTimePattern);
    }

    public static LocalDateTime getStartDateTimeOfBeforeMonth() {
        return LocalDateTime.now().minusDays(LocalDate.now().getDayOfMonth() - 1).withHour(0).withMinute(0).withSecond(0).minusMonths(1);
    }

    public static String getStartDateTimeOfBeforeMonthStr() {
        return getStartDateTimeOfBeforeMonth().format(dateTimePattern);
    }

    public static LocalDateTime getStartDateTimeOfAfterMonth() {
        return LocalDateTime.now().minusDays(LocalDate.now().getDayOfMonth() - 1).withHour(0).withMinute(0).withSecond(0).plusMonths(1);
    }

    public static String getStartDateTimeOfAfterMonthStr() {
        return getStartDateTimeOfAfterMonth().format(dateTimePattern);
    }

    public static LocalDate getFirstDayOfCurrentMonth() {
//        return LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1).plusMonths(1);
        return LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1);
    }

    public static String getFirstDayOfCurrentMonthStr() {
        return getFirstDayOfCurrentMonth().format(datePattern);
    }


    public static LocalDate getFirstDayOfBeforeMonth() {
//        return LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1).minusMonths(1).plusMonths(1);
        return LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1).minusMonths(1);
    }

    public static String getFirstDayOfBeforeMonthStr() {
        return getFirstDayOfBeforeMonth().format(datePattern);
    }

    public static String getFirstDayOfBeforeMonthStr(String pattern) {
        return getFirstDayOfBeforeMonth().format(DateTimeFormatter.ofPattern(pattern));
    }


    public static LocalDate getLastDayOfBeforeMonth() {
        return LocalDate.now().minusDays(LocalDate.now().getDayOfMonth());
    }

    public static String getLastDayOfBeforeMonthStr(String pattern) {
        return LocalDate.now().minusDays(LocalDate.now().getDayOfMonth()).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static Long getRemainSecondsInToday() {
        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);
    }

    /**
     * 获取当前月第一天
     */
    public static String getFirstDay(){
        return LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).format(datePattern);
    }

    /**
     * 获取当前月最后一天
     */
    public static String getLastDay(){
        return LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).format(datePattern);
    }

}
