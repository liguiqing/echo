package org.echo.ddd.domain.events;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : EventHandlers Test")
class EventHandlersTest {

    @Test
    public void test()throws Exception{
        final LocalDate now = LocalDate.now();
        final CountDownLatch cd = new CountDownLatch(8);
        final CountDownLatch cd2 = new CountDownLatch(5);
        final CountDownLatch cd3 = new CountDownLatch(3);

        EventHandlers.getInstance().register(new DomainEventHandler<Test2Created>() {
            @Override
            @Subscribe
            @AllowConcurrentEvents
            public void on(Test2Created event) {
                assertEquals(now,event.getNow());
                cd.countDown();
                cd2.countDown();
            }
        });

        EventHandlers.getInstance().register(new DomainEventHandler<Test1Created>() {
            @Override
            @Subscribe
            @AllowConcurrentEvents
            public void on(Test1Created event) {
                assertEquals(now,event.getNow());
                cd.countDown();
                cd2.countDown();
            }
        });
        GuavaEventHandler<Test1Created> handler = new GuavaEventHandler<Test1Created>() {

            @Override
            protected void when(Test1Created event) {
                assertEquals(now,event.getNow());
                cd.countDown();
                cd3.countDown();
            }
        };
        assertNotEquals(new Test1Created(now),new Test1Created(now));

        EventHandlers.getInstance().register(handler);
        EventHandlers.getInstance().post(new Test1Created(now));
        EventHandlers.getInstance().post(new Test1Created(now));
        EventHandlers.getInstance().post(new Test1Created(now));
        EventHandlers.getInstance().unregister(handler);
        EventHandlers.getInstance().post(new Test1Created(now));
        EventHandlers.getInstance().post(new Test1Created(now));
        cd.await();
        cd2.await();
        cd3.await();
        assertEquals(0,cd.getCount());
        assertEquals(0,cd2.getCount());
        assertEquals(0,cd3.getCount());

        EventHandlers.setEventBus(new EventBus() {});


    }
}