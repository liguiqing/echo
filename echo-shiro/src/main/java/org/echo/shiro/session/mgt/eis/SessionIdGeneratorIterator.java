package org.echo.shiro.session.mgt.eis;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.echo.util.CollectionsUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */

public class SessionIdGeneratorIterator implements SessionIdGenerator {
    private List<SessionIdGenerator> sessionIdGenerators;

    private SessionIdGenerator uuidSessionIdGenerator = new JavaUuidSessionIdGenerator();

    public SessionIdGeneratorIterator(Optional<List<SessionIdGenerator>> sessionIdGenerators){
        sessionIdGenerators.ifPresent(sessionIdGenerators1 -> this.sessionIdGenerators = sessionIdGenerators1);
    }

    public Serializable generateId(Session session){
        if(CollectionsUtil.isNotNullAndNotEmpty(this.sessionIdGenerators)){
            for(SessionIdGenerator sessionIdGenerator:sessionIdGenerators){
                Serializable sessionId = sessionIdGenerator.generateId(session);
                if(sessionId != null){
                    return sessionId;
                }
            }
        }
        return this.uuidSessionIdGenerator.generateId(session);
    }
}