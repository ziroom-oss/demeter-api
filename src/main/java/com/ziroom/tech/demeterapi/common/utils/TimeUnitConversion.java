package com.ziroom.tech.demeterapi.common.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author daijiankun
 * @date 9/3/2020
 * @description
 */
@UtilityClass
public class TimeUnitConversion {

    private static final Long MINUTES_OF_ONE_HOURS = 60L;

    public static String minuteToHour(Long minutes) {
        StringBuilder rtv = new StringBuilder();
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(0);
        format.setRoundingMode(RoundingMode.FLOOR);
        String hourPart = format.format(minutes.doubleValue() / MINUTES_OF_ONE_HOURS);
        long minutePart = minutes % MINUTES_OF_ONE_HOURS;
        if (StringUtils.isNotEmpty(hourPart) && !NumberUtils.INTEGER_ZERO.equals(Integer.parseInt(hourPart))) {
            rtv.append(hourPart).append("h");
        }
        if (minutePart != 0) {
            rtv.append(minutePart).append("m");
        }
        return rtv.toString();
    }

    public static Long hourToMinute(Double hours) {
        return new Double(hours * MINUTES_OF_ONE_HOURS).longValue();
    }
}
