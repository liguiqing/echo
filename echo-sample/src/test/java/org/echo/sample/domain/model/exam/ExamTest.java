package org.echo.sample.domain.model.exam;

import org.echo.ddd.domain.id.IdPrefix;
import org.echo.ddd.domain.id.Identities;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : Exam Test")
class ExamTest {

    @Test
    public void testConstructorExamId()throws Exception{
        final ExamId examId = new ExamId(Identities.genId(IdPrefix.ExamId));
        //final CountDownLatch cd = new CountDownLatch(1);
//        EventHandlers.getInstance().register(new DomainEventHandler<ExamCreated>() {
//            @Override
//            @Subscribe
//            @AllowConcurrentEvents
//            public void on(ExamCreated event) {
//                assertEquals(examId,event.getExamId());
//                cd.countDown();
//            }
//        });

        Exam exam1 = new Exam(examId);
        //cd.await();
        //assertEquals(0,cd.getCount());
    }
}