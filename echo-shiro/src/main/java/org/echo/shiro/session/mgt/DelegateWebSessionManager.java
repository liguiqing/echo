package org.echo.shiro.session.mgt;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;

/**
 * 使用DefaultWebSessionManager 为代理实现的 WebSessionManager ,
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
public abstract class DelegateWebSessionManager extends DefaultSessionManager implements WebSessionManager {

    @Delegate
    protected volatile DefaultWebSessionManager delegate;
}