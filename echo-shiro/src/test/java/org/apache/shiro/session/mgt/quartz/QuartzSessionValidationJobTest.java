package org.apache.shiro.session.mgt.quartz;

import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo-3rdext : 3rd QuartzSessionValidationJob test")
class QuartzSessionValidationJobTest {

    @Test
    void execute() throws Exception {
        QuartzSessionValidationJob validation = new QuartzSessionValidationJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        JobDataMap jobDataMap = mock(JobDataMap.class);
        ValidatingSessionManager sessionManager = mock(ValidatingSessionManager.class);
        when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
        when(jobDataMap.get(any(String.class))).thenReturn(sessionManager);
        validation.execute(context);

    }
}