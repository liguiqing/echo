package org.echo.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class Shiros {

    public static  Subject getSubject(){
        Subject subject = SecurityUtils.getSubject();
        log.debug(subject.toString());
        return subject;
    }

    public static boolean isAuthenticated(){
        Subject subject = getSubject();
        return subject.isAuthenticated();
    }
}