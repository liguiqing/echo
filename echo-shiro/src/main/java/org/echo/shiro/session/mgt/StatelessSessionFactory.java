package org.echo.shiro.session.mgt;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SimpleSession;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class StatelessSessionFactory implements SessionFactory {

    @Override
    public Session createSession(SessionContext sessionContext) {
        if (sessionContext != null) {
            String host = sessionContext.getHost();
            if (host != null) {
                log.info("Create ");
                return new StatelessSession(sessionContext,host);
            }
        }
        return new SimpleSession();
    }
}