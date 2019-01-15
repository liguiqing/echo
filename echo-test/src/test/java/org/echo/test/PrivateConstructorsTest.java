package org.echo.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
class PrivateConstructorsTest {

    @Test
    void exec() {
        assertThrows(Exception.class,()->new PrivateConstructors().exec(PrivateConstractorBean.class));
    }
}