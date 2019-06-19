/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package org.apache.shiro.session.mgt.quartz;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
  
/** 
 * 基于Quartz 2.* 版本的实现 
 */
@Slf4j
@Getter
@Setter
public class QuartzSessionValidationScheduler implements SessionValidationScheduler {  
  
    public static final long DEFAULT_SESSION_VALIDATION_INTERVAL = DefaultSessionManager.DEFAULT_SESSION_VALIDATION_INTERVAL;  
    private static final String JOB_NAME = "SessionValidationJob";  
    private static final String SESSION_MANAGER_KEY = QuartzSessionValidationJob.SESSION_MANAGER_KEY;
    private Scheduler scheduler;  
    private boolean schedulerImplicitlyCreated = false;  
  
    private boolean enabled = false;  
    private ValidatingSessionManager sessionManager;  
    private long sessionValidationInterval = DEFAULT_SESSION_VALIDATION_INTERVAL;

    public QuartzSessionValidationScheduler() {

    }

    public QuartzSessionValidationScheduler(ValidatingSessionManager sessionManager) {  
        this.sessionManager = sessionManager;  
    }  
  
    protected Scheduler getScheduler() throws SchedulerException {
        if (this.scheduler == null) {  
            this.scheduler = StdSchedulerFactory.getDefaultScheduler();  
            this.schedulerImplicitlyCreated = true;  
        }  
        return this.scheduler;  
    }

    @Override
    public void enableSessionValidation() {
        log.debug("Echo Scheduling session validation job using Quartz with session validation interval of [{}]ms...",this.sessionValidationInterval);

        try {  
            var trigger = TriggerBuilder.newTrigger()
                    .startNow()
                    .withIdentity(JOB_NAME, Scheduler.DEFAULT_GROUP)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(sessionValidationInterval))
                    .build();
  
            var detail = JobBuilder.newJob(QuartzSessionValidationJob.class)
                    .withIdentity(JOB_NAME, Scheduler.DEFAULT_GROUP).build();  
            detail.getJobDataMap().put(SESSION_MANAGER_KEY, this.getSessionManager());
            getScheduler().scheduleJob(detail, trigger);
            if (this.schedulerImplicitlyCreated) {  
                scheduler.start();
                log.debug("Successfully started implicitly created Echo Quartz Scheduler instance.");
            }  
            this.enabled = true;
            log.debug("Session validation job successfully scheduled with Quartz.");
        } catch (SchedulerException e) {
            log.error("Error starting the Echo Quartz session validation job.  Session validation may not occur.", e);
        }  
    }  

    @Override
    public void disableSessionValidation() {
        log.debug("Stopping Echo Quartz session validation job...");
        try {  
            getScheduler();
            if (this.scheduler == null) {
                log.warn("getScheduler() method returned a null Echo Quartz scheduler, which is unexpected.  Please check your configuration and/or implementation.  Returning quietly since there is no validation job to remove (scheduler does not exist).");
                return;
            }  
        } catch (SchedulerException e) {  
            log.warn("Unable to acquire Echo Quartz Scheduler.  Ignoring and returning (already stopped?)", e);
            return;
        }  
        try {
            this.scheduler.unscheduleJob(new TriggerKey(JOB_NAME, "DEFAULT"));
            log.debug("Echo Quartz session validation job stopped successfully.");
        } catch (SchedulerException e) {  
            log.warn("Could not cleanly remove SessionValidationJob from Echo Quartz scheduler.  Ignoring and stopping.", e);
        }  
  
        this.enabled = false;  
  
        if (this.schedulerImplicitlyCreated) {
            try {
                this.scheduler.shutdown();
            } catch (SchedulerException e) {
                log.warn("Unable to cleanly shutdown implicitly created Echo Quartz Scheduler instance.", e);
            } finally {
                setScheduler(null);
                this.schedulerImplicitlyCreated = false;
            }
        }
    }
}