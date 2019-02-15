package org.echo.sample.boot;

import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

@SpringBootTest(classes = SampleApplication.class)
class SampleApplicationTest extends AbstractConfigurationsTest {

    @Test
    void test(){
        assertTrue(true);
    }
}