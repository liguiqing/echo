package org.echo.exam.domain.model.exam;

import org.echo.exam.config.AppConfigurations;
import org.echo.share.config.CacheConfigurations;
import org.echo.share.config.DataSourceConfigurations;
import org.echo.share.id.commons.ExamId;
import org.echo.spring.cache.secondary.SecondaryCacheAutoConfiguration;
import org.echo.test.config.JunitTestConfigurations;
import org.echo.test.repository.AbstractRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
        DataSourceConfigurations.class,
        SecondaryCacheAutoConfiguration.class,
        CacheConfigurations.class,
        AppConfigurations.class
        }))
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml"})
@Transactional
@Rollback
@DisplayName("Echo : Exam module ExamRepository test")
class ExamRepositoryTest extends AbstractRepositoryTest {

    @Value("${jdb.url}")
    private String jdbcUrl;

    @Autowired
    ExamRepository repository;

    @Test
    void test(){
        ExamId examId = repository.nextIdentity();
        assertNotNull(examId);
        Exam exam = new Exam(examId);
        repository.save(exam);
        Exam exam1 = repository.loadOf(examId);
        assertNotNull(exam1);
        assertEquals(exam,exam1);
        repository.loadOf(examId);
        repository.loadOf(examId);
        for(int i =1000;i>0;i--){
            repository.loadOf(examId);
        }
    }
}