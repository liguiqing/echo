package org.echo.shiro.web.session.mgt;

import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.util.RequestPairSource;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class TestSessionKey implements SessionKey, RequestPairSource{

    @Override
    public Serializable getSessionId() {
        return null;
    }

    @Override
    public ServletRequest getServletRequest() {
        return null;
    }

    @Override
    public ServletResponse getServletResponse() {
        return null;
    }
}