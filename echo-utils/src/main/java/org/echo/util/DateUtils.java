package org.echo.util;

import java.time.*;
import java.util.Date;

/**
 * 日期时间处理工具
 *
 * @author Liguiqing
 * @since V1.0
 */

public class DateUtils {

    private DateUtils(){
        throw new AssertionError("No org.echo.util.DateUtils instances for you!");
    }

    /**
     *
     * @param localDate {@link LocalDate}
     * @return Date {@link Date}
     */
    public static Date fromLocalDate(LocalDate localDate){
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Instant instant1 = zonedDateTime.toInstant();
        Date from = Date.from(instant1);
        return  from;
    }

    /**
     * 将日期转换到 LocalDate
     * @param date {@link Date}
     * @return
     */
    public static LocalDate toLocalDate(Date date){
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    /**
     * 获得某天最大时间 2019-10-15 23:59:59
     * @param date {@link Date}
     * @return Date {@link Date}
     */
    public static Date endOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得某天最小时间 2019-10-15 00:00:00
     * @param date {@link Date}
     * @return Date {@link Date}
     */
    public static Date startOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }
}