package com.ziroom.tech.demeterapi.common.utils;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author chenx34
 * @date 2020/8/17 17:06
 */
@UtilityClass
public class DateUtils {

    /**
     * yyyy-mm-dd HH:mm:ss
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-mm-dd HH:mm:ss";

    /**
     * yyyy-MM-dd
     */
    public static final String YYYY_MM_DD ="yyyy-MM-dd";

    /**
     * 星期一英文简写
     */
    public static final String MON = "mon";
    /**
     * 星期二英文简写
     */
    public static final String TUE = "tue";
    /**
     * 星期三英文简写
     */
    public static final String WED = "wed";
    /**
     * 星期四英文简写
     */
    public static final String THU = "thu";
    /**
     * 星期五英文简写
     */
    public static final String FRI = "fri";
    /**
     * 星期六英文简写
     */
    public static final String SAT = "sat";
    /**
     * 星期日英文简写
     */
    public static final String SUN = "sun";

    /**
     * 判断传入的开始时间和结束时间是否在当前周内
     * @param dateFrom 开始时间
     * @param dateTo 结束时间
     * @return 如果在当前周-true 不在-false 传入的开始时间和结束时间不能为空，为空将直接返回false
     */
    public static boolean isCurrentWeek(Date dateFrom, Date dateTo) {
        if (Objects.isNull(dateFrom) || Objects.isNull(dateTo)) {
            return false;
        }
        Pair<Date, Date> currentWeek = getCurrentWeekStartAndEnd();
        // FIXME 应该前端解决
        long start = LocalDateTime.of(convertDateToLocalDate(dateFrom), LocalTime.of(0, 0)).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        long end = LocalDateTime.of(convertDateToLocalDate(dateTo), LocalTime.of(0, 0)).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        return currentWeek.getKey().getTime() == start && currentWeek.getValue().getTime() == end;
    }

    /**
     * 获取指定周数对应的周一和周日的日期
     * 当传入的周数为0时默认查询当前周的周一和周日的日期
     * @param weekNumber 周数
     * @return key为周一日期，value为周日日期
     */
    public static Pair<Date, Date> getWeekStartAndEndByWeekNumber(int weekNumber) {
        if(weekNumber == NumberUtils.INTEGER_ZERO) {
            return getCurrentWeekStartAndEnd();
        }
        LocalDate now = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int nowWeekNumber = LocalDate.now().get(weekFields.weekOfWeekBasedYear());
        int diff = Math.abs(weekNumber - nowWeekNumber);
        int day = diff * 7;
        now = weekNumber > nowWeekNumber ? now.plusDays(day) : now.minusDays(day);
        return getWeekStartAndEnd(now);
    }

    /**
     * 获取本周周一的日期和周日的日期
     * @return key为周一日期，value为周日日期
     */
    public static Pair<Date, Date> getCurrentWeekStartAndEnd() {
        LocalDate now = LocalDate.now();
        return getWeekStartAndEnd(now);
    }

    /**
     * 根据传入的日期计算出日期对应周的周一和周日的日期
     * @param date 需要计算的日期
     * @return key为周一的日期，value为周日的日期
     */
    public static Pair<Date, Date> getWeekStartAndEnd(LocalDate date) {
        LocalDate monday = date.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusDays(NumberUtils.LONG_ONE);
        LocalDate sunday = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(NumberUtils.LONG_ONE);
        ZoneId zone = ZoneId.systemDefault();
        Instant mondayInstant = monday.atStartOfDay(zone).toInstant();
        Instant sundayInstant = sunday.atStartOfDay(zone).toInstant();
        return Pair.of(Date.from(mondayInstant), Date.from(sundayInstant));
    }

    /**
     * 获取传入的日期上一周的周一和周日的日期
     * @param localDate 需要计算的日期
     * @return key为周一的日期，value为周日的日期
     */
    public static Pair<Date, Date> getPreWeekStartAndEnd(LocalDate localDate) {
        localDate = localDate.minusDays(7L);
        return getWeekStartAndEnd(localDate);
    }

    /**
     * 获取传入日期的下一周周一和周日的日期
     * @param localDate 需要计算的日期
     * @return key为周一的日期，value为周日的日期
     */
    public static Pair<Date, Date> getNextWeekStartAndEnd(LocalDate localDate) {
        localDate = localDate.plusDays(7L);
        return getWeekStartAndEnd(localDate);
    }

    /**
     * 将date转换成LocalDate
     * @param source 需要转成LocalDate的date
     * @return LocalDate
     */
    public static LocalDate convertDateToLocalDate(Date source) {
        Instant instant = source.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.toLocalDate();
    }

    /**
     * 根据传入的pattern格式化date
     * @param date 需要进行格式化的date
     * @param pattern 格式
     * @return 字符串
     */
    public static String format(Date date, String pattern) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
    }

    /**
     * 根据传入的时间范围，将包含开始时间和结束时间在内的日期字符串
     * 示例： dateFrom-2020-08-20 dateTo-2020-08-27
     * List的内容为[2020-08-20, 2020-08-21, 2020-08-22, 2020-08-23, 2020-08-24, 2020-08-25, 2020-08-26, 2020-08-27]
     * 日期字符串格式为yyyy-MM-DD
     * @param dateFrom 开始时间 格式为yyyy-MM-DD的字符串
     * @param dateTo 结束时间 格式为yyyy-MM-DD的字符串
     * @return List<String>
     */
    public static List<String> getPeriodOfTimeStr(String dateFrom, String dateTo) {
        List<String> list = Lists.newArrayList();
        LocalDate startDate = LocalDate.parse(dateFrom);
        LocalDate endDate = LocalDate.parse(dateTo);
        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return Lists.newArrayList(dateFrom);
        }
        Stream.iterate(startDate, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> list.add(f.toString()));
        return list;
    }

    /**
     * 根据传入的时间范围，将包含开始时间和结束时间，date对象将会转换成yyyy-MM-DD的形式
     * 示例： dateFrom-2020-08-20 dateTo-2020-08-27
     * List的内容为[2020-08-20, 2020-08-21, 2020-08-22, 2020-08-23, 2020-08-24, 2020-08-25, 2020-08-26, 2020-08-27]
     * 日期字符串格式为yyyy-MM-DD
     * @param dateFrom 开始时间 格式为yyyy-MM-DD的字符串
     * @param dateTo 结束时间 格式为yyyy-MM-DD的字符串
     * @return List<String>
     */
    public static List<String> getPeriodOfDate(Date dateFrom, Date dateTo) {
        List<String> list = Lists.newArrayList();
        LocalDate startDate = convertDateToLocalDate(dateFrom);
        LocalDate endDate = convertDateToLocalDate(dateTo);
        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(startDate, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> list.add(f.toString()));
        return list;
    }

    /**
     * 根据传入的日期获取对应的星期数
     * @param source 需要转换的日期
     * @return 星期数对应的英语简写
     */
    public String getDayOfWeekEnglish(Date source) {
        LocalDate localDate = convertDateToLocalDate(source);
        int dayOfWeek = localDate.getDayOfWeek().getValue();
        switch (dayOfWeek) {
            case 1: return MON;
            case 2: return TUE;
            case 3: return WED;
            case 4: return THU;
            case 5: return FRI;
            case 6: return SAT;
            case 7: return SUN;
            default: return "未知天数";
        }
    }

    /**
     * 将字符串转换成LocalDate对象
     * @param dateStr 需要进行转换的日期对象
     * @param pattern 时间格式化的格式
     * @return LocalDate
     */
    public static LocalDate convertStrToLocalDate(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * 将localDate对象转换成date
     * @param localDate 需要进行转换的localDate对象
     * @return Date，如果传入的lcoalDate为null时，将返回null
     */
    public static Date convertLocalDateToDate(LocalDate localDate) {
        if(Objects.isNull(localDate)) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }
}
