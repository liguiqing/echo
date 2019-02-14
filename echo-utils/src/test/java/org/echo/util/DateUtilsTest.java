package org.echo.util;

import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : DateUtils Test")
class DateUtilsTest {

    @Test
    void fromLocalDate() {
        assertNotNull(DateUtils.fromLocalDate(LocalDate.now()));
        assertThrows(Exception.class,()->new PrivateConstructors().exec(DateUtils.class));
    }
}