package org.echo.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("PrivateConstructors Test")
class PrivateConstructorsTest {

    @Test
    void exec()throws Exception {
        assertThrows(Exception.class,()->new PrivateConstructors().exec(PrivateConstractorBean.class));
        new PrivateConstructors().exec(ArrayList.class);
    }
}