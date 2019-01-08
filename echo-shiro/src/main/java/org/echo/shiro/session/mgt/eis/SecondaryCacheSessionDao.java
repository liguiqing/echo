package org.echo.shiro.session.mgt.eis;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.echo.spring.cache.secondary.SecondaryCache;
import org.echo.spring.cache.secondary.SecondaryCacheManager;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Secondary base CacheSessionDao
 *
 * @author Liguiqing
 * @since V1.0
 */

public class SecondaryCacheSessionDao extends CachingSessionDAO {

    public SecondaryCacheSessionDao(SecondaryCacheManager cacheManager){
        setCacheManager(new AbstractCacheManager() {
            @Override
            protected Cache<Serializable, Session> createCache(String name) throws CacheException {
                SecondaryCache cache = (SecondaryCache)cacheManager.getCache(name);
                return new Cache<Serializable, Session>() {
                    @Override
                    public Session get(Serializable serializable) throws CacheException {
                        return (Session)cache.get(serializable).get();
                    }

                    @Override
                    public Session put(Serializable serializable, Session session) throws CacheException {
                        return (Session) cache.putIfAbsent(serializable,session).get();
                    }

                    @Override
                    public Session remove(Serializable serializable) throws CacheException {
                        Session session = (Session)cache.get(serializable).get();
                        cache.evict(serializable);
                        return session;
                    }

                    @Override
                    public void clear() throws CacheException {
                        cache.clear();
                    }

                    @Override
                    public int size() {
                        return 0;
                    }

                    @Override
                    public Set<Serializable> keys() {
                        return Collections.emptySet();
                    }

                    @Override
                    public Collection<Session> values() {
                        return Collections.emptySet();
                    }
                };
            }
        });
    }

    @Override
    protected void doUpdate(Session session) {

    }

    @Override
    protected void doDelete(Session session) {

    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return null;
    }
}