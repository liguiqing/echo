package org.echo.sample.domain.model.exam;

import org.echo.ddd.domain.id.AssociationId;
import org.echo.ddd.domain.id.Identities;
import org.echo.ddd.domain.id.Identity;
import org.echo.hibernate.converter.HibernateEnum;
import org.echo.sample.config.AppConfigurations;
import org.echo.share.config.CacheConfigurations;
import org.echo.share.config.DataSourceConfigurations;
import org.echo.spring.cache.config.SecondaryCacheAutoConfiguration;
import org.echo.test.repository.AbstractRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

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
        ExamScope scope = HibernateEnum.fromValue(ExamScope.class,exam.getScope().getValue());
        assertEquals(scope,exam1.getScope());
        repository.loadOf(examId);
        repository.loadOf(examId);
        for(int i =1000;i>0;i--){
            repository.loadOf(examId);
        }
        Identity<String> projectId2 = Identities.genId("PRJ", AssociationId.class);
        exam1.joinProject(projectId2);
        repository.save(exam1);
        Exam exam2 = repository.loadOf(examId);
        assertEquals(projectId2,exam2.getProject());
    }
}