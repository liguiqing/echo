package org.echo.hibernate.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : RedisBaseDistributedLock test")
class HibernateEnumTest {

    @Test
    void fromValue() {
        assertEquals(HibernateEnumIntegerBean.One, HibernateEnum.fromValue(HibernateEnumIntegerBean.class,1));
        assertEquals(HibernateEnumIntegerBean.Two, HibernateEnum.fromValue(HibernateEnumIntegerBean.class,2));
        assertNotEquals(HibernateEnumIntegerBean.One, HibernateEnum.fromValue(HibernateEnumIntegerBean.class,2));
        assertThrows(IllegalArgumentException.class,()->HibernateEnum.fromValue(HibernateEnumIntegerBean.class,3));
        assertThrows(IllegalArgumentException.class,()->HibernateEnum.fromValue(HibernateEnumIntegerBean.class,0));

        assertEquals(HibernateEnumLongBean.One,HibernateEnum.fromValue(HibernateEnumLongBean.class,1L));
        assertEquals(HibernateEnumLongBean.Two,HibernateEnum.fromValue(HibernateEnumLongBean.class,2L));

        assertEquals(HibernateEnumStringBean.Today,HibernateEnum.fromValue(HibernateEnumStringBean.class, LocalDate.now().toString()));
        assertEquals(HibernateEnumStringBean.Tomorrow, HibernateEnum.fromValue(HibernateEnumStringBean.class, LocalDate.now().plusDays(1).toString()));
    }
}