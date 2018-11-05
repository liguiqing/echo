package org.echo.commons.domain.model.exam;

import org.echo.commons.config.AppConfigurations;
import org.echo.share.config.CacheConfigurations;
import org.echo.share.config.DataSourceConfigurations;
import org.echo.test.config.JunitTestConfigurations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        JunitTestConfigurations.class,
        DataSourceConfigurations.class,
        CacheConfigurations.class,
        AppConfigurations.class
        })
//@Transactional
//@Rollback
class ExamRepositoryTest {

    @Value("${jdb.url}")
    private String jdbcUrl;
    @Test
    void test(){

    }
}