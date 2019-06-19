package org.echo.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : DateUtils Test")
class DateUtilsTest {

    @Test
    void fromLocalDate() {
        assertNotNull(DateUtils.fromLocalDate(LocalDate.now()));
        assertThrows(Exception.class,()->ClassUtils.newInstanceOf(DateUtils.class));
    }

    @Test
    void toLocalDate(){
        Date date = Calendar.getInstance().getTime();
        assertNotNull(DateUtils.toLocalDate(date));
    }

    @Test
    void endOfDay(){
        Date date = Calendar.getInstance().getTime();
        date = DateUtils.endOfDay(date);
        assertTrue(DateFormatUtils.format(date,"yyyy-MM-dd HH:mm:ss").endsWith("23:59:59"));
    }

    @Test
    void startOfDay(){
        Date date = Calendar.getInstance().getTime();
        date = DateUtils.startOfDay(date);
        assertTrue(DateFormatUtils.format(date,"yyyy-MM-dd HH:mm:ss").endsWith("00:00:00"));
    }
}