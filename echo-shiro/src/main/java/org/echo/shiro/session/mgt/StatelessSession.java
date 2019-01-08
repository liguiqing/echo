package org.echo.shiro.session.mgt;

import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SimpleSession;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Liguiqing
 * @since V1.0
 */
public class StatelessSession extends SimpleSession {

    private SessionContext sessionContext;

    public StatelessSession(SessionContext sessionContext,String host) {
        super(host);
        this.sessionContext = sessionContext;
    }

    public HttpServletRequest getRequest(){
        return (HttpServletRequest)sessionContext.get("org.apache.shiro.web.session.mgt.DefaultWebSessionContext.SERVLET_REQUEST");
    }
}