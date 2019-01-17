package org.echo.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
}