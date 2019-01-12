package org.echo.shiro.web.session.mgt;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.echo.util.UserAgentUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Objects;

/**
 * 可以兼容WebApp,移动APP,微信小程序,微信公众号的无状态Session Manager
 *
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
@Slf4j
@Getter
public class StatelessWebSessionManager extends DefaultSessionManager implements WebSessionManager {

    private String sessionToken ;

    private String referencedSessionIdSource;

    private interface Excludes {
        Serializable getSessionId(SessionKey key);
        boolean isServletContainerSessions();
    }

    @Delegate(excludes= Excludes.class)
    private final DefaultWebSessionManager delegate = new DefaultWebSessionManager();

    public StatelessWebSessionManager() {
        this("x-auth-token","header");
    }

    @Override
    public Serializable getSessionId(SessionKey key){
        HttpServletRequest request = WebUtils.getHttpRequest(key);
        if(UserAgentUtils.isBroswer(request)){
            return this.delegate.getSessionId(key);
        }

        Serializable sessionId = key.getSessionId();
        if(Objects.isNull(sessionId)){
            sessionId = this.getSessionId(request);
        }

        if(!Objects.isNull(sessionId)){
            HttpServletResponse response = WebUtils.getHttpResponse(key);
            response.setHeader(this.sessionToken, sessionId.toString());

        }
        return sessionId;
    }

    @Override
    protected void onStart(Session session, SessionContext context){
        HttpServletRequest request = WebUtils.getHttpRequest(context);
        if(UserAgentUtils.isBroswer(request)){
            this.delegate.start(context);
            return;
        }

        HttpServletResponse response = WebUtils.getHttpResponse(context);
        Serializable sessionId = session.getId();
        this.storeSessionId(sessionId, response);
        request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
    }

    @Override
    protected void onStop(Session session, SessionKey key) {
        HttpServletRequest request = WebUtils.getHttpRequest(key);
        if(UserAgentUtils.isBroswer(request)){
            this.delegate.stop(key);
            return;
        }

        if (WebUtils.isHttp(key)) {
            log.debug("Session has been stopped (subject logout or explicit stop).  Removing session ID cookie.");
            this.removeSessionId(WebUtils.getHttpResponse(key));
        } else {
            log.debug("SessionKey argument is not HTTP compatible or does not have an HTTP request/response pair. Session ID cookie will not be removed due to stopped session.");
        }
    }

    @Override
    public boolean isServletContainerSessions() {
        HttpServletRequest request =ServletUtilWrapper.getRequest();
        if(UserAgentUtils.isBroswer(request)){
            return this.delegate.isServletContainerSessions();
        }
        return false;
    }

    /**
     * 移除sessionId 并设置为 deleteMe 标识
     *
     * @param response HttpServletResponse
     */
    private void removeSessionId( HttpServletResponse response) {
        response.setHeader(this.sessionToken, "deleteMe");
    }

    private Serializable getSessionId(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if(UserAgentUtils.isBroswer(httpRequest)){
            return null;
        }

        String id = httpRequest.getHeader(this.sessionToken);

        if (StringUtils.isEmpty(id)) {
            log.debug("Session Id is null ");

            return null;
        } else {
            //如果请求头中有 authToken 则其值为sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, referencedSessionIdSource);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, Boolean.FALSE);
            log.debug("Session Id {}",id);
            return id;
        }
    }


    /**
     * 把sessionId 放入 response header 中
     *
     * @param currentId Serializable
     * @param response HttpServletResponse
     */
    private void storeSessionId(Serializable currentId, HttpServletResponse response) {
        Preconditions.checkNotNull(currentId, "sessionId cannot be null when persisting for subsequent requests.");
        String idString = currentId.toString();
        response.setHeader(this.sessionToken, idString);
        log.info("Set session ID header for session with id {}", idString);
    }
}