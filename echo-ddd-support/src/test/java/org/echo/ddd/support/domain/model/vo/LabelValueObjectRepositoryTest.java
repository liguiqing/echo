package org.echo.ddd.support.domain.model.vo;

import org.echo.ddd.support.config.DddSupportConfigurations;
import org.echo.share.config.DataSourceConfigurations;
import org.echo.xcache.config.AutoCacheConfigurations;
import org.echo.test.repository.AbstractRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                DataSourceConfigurations.class,
                AutoCacheConfigurations.class,
                DddSupportConfigurations.class
        }))
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml,classpath:/application-redis.yml"})
@Transactional
@Rollback
@DisplayName("Echo : LabelValueObjectRepository Test")
class LabelValueObjectRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private LabelValueObjectRepository labelValueObjectRepository;

    @Test
    public void test(){
        assertTrue(Boolean.TRUE);
        LabelValueObject lvo = new LabelValueObject(1,"a","Aa","A-A");
        labelValueObjectRepository.save(lvo);
        LabelValueObject lvo_ = labelValueObjectRepository.get(lvo.getTid());
        assertEquals(lvo,lvo_);
        labelValueObjectRepository.delete(lvo_.getTid(),lvo_.getDetail().getCategory());
        lvo_ = labelValueObjectRepository.get(lvo.getTid());
        assertNull(lvo_);

        for(int i = 9;i>0;i--){
            lvo = new LabelValueObject(i,"a-".concat(Integer.toString(i)),Integer.toString(i),"A-A");
            labelValueObjectRepository.save(lvo);
        }
        List<LabelValueObject> lvos = labelValueObjectRepository.findAllByCategoryOrderBySeq("A-A");
        assertEquals(9,lvos.size());
        int i = 1;
        for(LabelValueObject l:lvos){
            assertEquals("a-".concat(Integer.toString(i)),l.getDetail().getLabel());
            i++;
        }
        labelValueObjectRepository.updateCategory("A-A","A-B");
        lvos = labelValueObjectRepository.findAllByCategoryOrderBySeq("A-A");
        assertEquals(0,lvos.size());
        lvos = labelValueObjectRepository.findAllByCategoryOrderBySeq("A-B");
        assertEquals(9,lvos.size());
        while(i<1000){
            i++;
            labelValueObjectRepository.findAllByCategoryOrderBySeq("A-B");
        }
        labelValueObjectRepository.delete(lvos.get(1).getTid(),lvos.get(1).getDetail().getCategory());
    }
}