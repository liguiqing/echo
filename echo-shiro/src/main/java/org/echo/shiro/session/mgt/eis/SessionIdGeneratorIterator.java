package org.echo.shiro.session.mgt.eis;

import com.google.common.collect.Lists;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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
        this.sessionIdGenerators = sessionIdGenerators.orElse(Lists.newArrayList());
    }

    @Override
    public Serializable generateId(Session session){

        for(var sessionIdGenerator:sessionIdGenerators){
            var sessionId = sessionIdGenerator.generateId(session);
            if(!Objects.isNull(sessionId)){
                return sessionId;
            }
        }
        return this.uuidSessionIdGenerator.generateId(session);
    }
}