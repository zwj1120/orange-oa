package com.xcr.orange.oa.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @Author xiaochaorou7
 * @Description
 * @Date 2024/8/28
 */
public class DateUtil {

    public static String getCurrentDate(String format) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return currentDate.format(dateTimeFormatter);
    }

}
