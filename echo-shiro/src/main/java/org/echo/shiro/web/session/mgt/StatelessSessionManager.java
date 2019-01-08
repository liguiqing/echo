package org.echo.shiro.web.session.mgt;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.echo.shiro.session.mgt.DelegateWebSessionManager;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 可以兼容WebApp,移动APP,微信小程序,微信公众号的无状态Session Manager
 *
 * @author Liguiqing
 * @since V1.0
 */

@Slf4j
@Getter
public class StatelessSessionManager extends DelegateWebSessionManager {

    private String sessionToken ;

    private String referencedSessionIdSource;

    public StatelessSessionManager() {
        this(new DefaultWebSessionManager());
    }

    public StatelessSessionManager(DefaultWebSessionManager delegate) {
        this(delegate,"sessionToken","Stateless request");
    }

    public StatelessSessionManager(DefaultWebSessionManager delegate, String sessionToken, String referencedSessionIdSource) {
        super(delegate);
        this.sessionToken = sessionToken;
        this.referencedSessionIdSource = referencedSessionIdSource;
    }


    @Override
    public Serializable getSessionId(SessionKey key) {
        Serializable id = super.getSessionId(key);
        if (id == null && WebUtils.isWeb(key)) {
            ServletRequest request = WebUtils.getRequest(key);
            return getSessionId(request);
        }
        return id;
    }

    private Serializable getSessionId(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String id = httpRequest.getHeader(this.sessionToken);

        if (StringUtils.isEmpty(id)) {
            log.debug("Stateless Session Id is null ");
            return null;
        } else {
            //如果请求头中有 authToken 则其值为sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, referencedSessionIdSource);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            log.debug("Stateless Session Id {}",id);
            return id;
        }
    }
}