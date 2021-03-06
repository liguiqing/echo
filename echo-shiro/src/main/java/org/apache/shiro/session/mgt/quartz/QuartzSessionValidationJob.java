/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package org.apache.shiro.session.mgt.quartz;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/** 
 * 基于Quartz 2.* 版本的实现 
 *  
 */
@Slf4j
public class QuartzSessionValidationJob implements Job {  
  
    /** 
     * Key used to store the session manager in the job data map for this job. 
     */  
    public static final String SESSION_MANAGER_KEY = "sessionManager";  
  
    /*-------------------------------------------- 
    |    I N S T A N C E   V A R I A B L E S    | 
    ============================================*/

    /*-------------------------------------------- 
    |         C O N S T R U C T O R S           | 
    ============================================*/  
  
    /*-------------------------------------------- 
    |  A C C E S S O R S / M O D I F I E R S    | 
    ============================================*/  
  
    /*-------------------------------------------- 
    |               M E T H O D S               | 
    ============================================*/  
  
    /** 
     * Called when the job is executed by quartz. This method delegates to the <tt>validateSessions()</tt> method on the 
     * associated session manager. 
     *  
     * @param context  the Quartz job execution context for this execution.
     *
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {  
  
        var jobDataMap = context.getMergedJobDataMap();
        var sessionManager = (ValidatingSessionManager) jobDataMap.get(SESSION_MANAGER_KEY);
        if(sessionManager == null)
            return;

        log.debug("Executing session validation Quartz job...");
        sessionManager.validateSessions();
        log.debug("Session validation Quartz job complete.");
    }
  
}