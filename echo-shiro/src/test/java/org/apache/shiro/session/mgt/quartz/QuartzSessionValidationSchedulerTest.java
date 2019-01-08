package org.apache.shiro.session.mgt.quartz;

import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

@ExtendWith(MockitoExtension.class)
class QuartzSessionValidationSchedulerTest {

    @Test
    void enableSessionValidation() {
        ValidatingSessionManager sessionManager = mock(ValidatingSessionManager.class);
        QuartzSessionValidationScheduler scheduler = new QuartzSessionValidationScheduler(sessionManager);
        scheduler.enableSessionValidation();
        scheduler.enableSessionValidation();
    }

    @Test
    void disableSessionValidation()throws Exception{
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
    }
}