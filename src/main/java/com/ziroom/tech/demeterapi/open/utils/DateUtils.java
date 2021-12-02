package com.ziroom.tech.demeterapi.open.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.time.temporal.IsoFields.QUARTER_OF_YEAR;
import static java.time.temporal.IsoFields.QUARTER_YEARS;

/**
 * @author donghao
 */
public class DateUtils {

    public static FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public static ZoneId ZONE_UTC_P_8 = ZoneId.of("+8");

    public static Function<Date, LocalDate> DATE_TO_LOCAL_DATE = date -> date.toInstant().atZone(ZONE_UTC_P_8).toLocalDate();

    public static Function<Date, LocalDateTime> DATE_TO_LOCAL_DATE_TIME = date -> date.toInstant().atZone(ZONE_UTC_P_8).toLocalDateTime();

    public static DateTimeFormatter FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static DateTimeFormatter FORMATTER_YYYY_MM = DateTimeFormatter.ofPattern("yyyy-MM");

    public static DateTimeFormatter FORMATTER_YYYY = DateTimeFormatter.ofPattern("yyyy");

    public static DateTimeFormatter FORMATTER_YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static DateTimeFormatter FORMATTER_YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static Date toDate(String timeLabel) {
        LocalDateTime localDateTime = LocalDateTime.parse(timeLabel, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Date.from(localDateTime.atZone(ZONE_UTC_P_8).toInstant());
    }

    public static String dateToStr(Date date, String pattern) {
        return date.toInstant().atOffset(ZoneOffset.ofHours(8)).toLocalDateTime().format(DateTimeFormatter.ofPattern(pattern));
    }


    public static Date now() {
        return Date.from(LocalDateTime.now().atZone(ZONE_UTC_P_8).toInstant());
    }

    public static Function<LocalDate, String> GROUP_BY_DAY = FORMATTER_YYYY_MM_DD::format;

    public static Function<LocalDate, String> GROUP_BY_MONTH = FORMATTER_YYYY_MM::format;

    public static Function<LocalDate, String> GROUP_BY_QUARTERLY = date -> FORMATTER_YYYY.format(date)
            + " Q"
            + date.get(QUARTER_OF_YEAR);

    public static BiFunction<LocalDate, LocalDateTime, Stream<String>> DAYS_STREAM_BETWEEN_GIVEN = (begin, end) -> Stream.iterate(begin, d -> d.plusDays(1))
            .limit(ChronoUnit.DAYS.between(begin, end)).map(GROUP_BY_DAY);

    public static BiFunction<LocalDate, LocalDateTime, Stream<String>> MONTHS_STREAM_BETWEEN_GIVEN = (begin, end) -> Stream.iterate(begin, d -> d.plusMonths(1))
            .limit(ChronoUnit.MONTHS.between(begin, end)).map(GROUP_BY_MONTH);

    public static BiFunction<LocalDate, LocalDateTime, Stream<String>> QUARTERLYS_STREAM_BETWEEN_GIVEN = (begin, end) -> Stream.iterate(begin, d -> d.plus(1, QUARTER_YEARS))
            .limit(IsoFields.QUARTER_YEARS.between(begin, end)).map(GROUP_BY_QUARTERLY);


    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (Exception e) {
        }

        return date;
    }
}
