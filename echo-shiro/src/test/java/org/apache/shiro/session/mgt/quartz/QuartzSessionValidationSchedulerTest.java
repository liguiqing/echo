package org.apache.shiro.session.mgt.quartz;

import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

@DisplayName("Echo-3rdext : 3rd QuartzSessionValidationScheduler Test")
class QuartzSessionValidationSchedulerTest {

    @Test
    void enableSessionValidation() throws Exception{
        QuartzSessionValidationScheduler spyQV = spy(new QuartzSessionValidationScheduler());
        when(spyQV.getScheduler()).thenThrow(new SchedulerException());
        spyQV.enableSessionValidation();

        ValidatingSessionManager sessionManager = mock(ValidatingSessionManager.class);
        QuartzSessionValidationScheduler scheduler = new QuartzSessionValidationScheduler(sessionManager);
        scheduler.enableSessionValidation();
        scheduler.enableSessionValidation();
        assertTrue(true);
    }

    @Test
    void disableSessionValidation()throws Exception{
        QuartzSessionValidationScheduler spyQV = spy(new QuartzSessionValidationScheduler());
        when(spyQV.getScheduler()).thenThrow(new SchedulerException()).thenReturn(null);
        spyQV.disableSessionValidation();
        spyQV.setScheduler(null);
        spyQV.disableSessionValidation();

        ValidatingSessionManager sessionManager = mock(ValidatingSessionManager.class);
        QuartzSessionValidationScheduler scheduler = new QuartzSessionValidationScheduler(sessionManager);
        scheduler.disableSessionValidation();

        Scheduler scheduler1 = mock(Scheduler.class);
        when(scheduler1.unscheduleJob(any(TriggerKey.class))).thenThrow(new SchedulerException()).thenReturn(true);
        scheduler.setScheduler(scheduler1);
        scheduler.disableSessionValidation();
        scheduler = new QuartzSessionValidationScheduler(sessionManager);
        scheduler.setSchedulerImplicitlyCreated(true);
        doThrow(new SchedulerException()).when(scheduler1).shutdown();
        scheduler.setScheduler(scheduler1);
        scheduler.disableSessionValidation();
        assertTrue(true);
    }
}