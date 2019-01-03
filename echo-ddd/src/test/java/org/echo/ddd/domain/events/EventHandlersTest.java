package org.echo.ddd.domain.events;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : EventHandlers Test")
class EventHandlersTest {

    @Test
    public void test()throws Exception{
        final LocalDate now = LocalDate.now();
        final CountDownLatch cd = new CountDownLatch(6);
        EventHandlers.getInstance().register(new DomainEventHandler<Test1Created>() {
            @Override
            @Subscribe
            @AllowConcurrentEvents
            public void on(Test1Created event) {
                assertEquals(now,event.getNow());
                cd.countDown();
            }
        });
        GuavaEventHandler handler;
        handler = new GuavaEventHandler<Test1Created>() {

            @Override
            protected void doOn(Test1Created event) {
                assertEquals(now,event.getNow());
                cd.countDown();
            }
        };
        EventHandlers.getInstance().register(handler);

        EventHandlers.getInstance().post(new Test1Created(now));
        EventHandlers.getInstance().post(new Test1Created(now));

        DomainEventHandler mockHandler = mock(DomainEventHandler.class);
        doNothing().when(mockHandler).on(any(Test1Created.class));
        EventHandlers.getInstance().register(mockHandler);
        doAnswer(invocation -> {
            assertFalse(true);
            return null;
        }).when(mockHandler).on(any(Test1Created.class));
        EventHandlers.getInstance().post(new Test1Created(now));
        cd.await();
        assertEquals(0,cd.getCount());
    }
}