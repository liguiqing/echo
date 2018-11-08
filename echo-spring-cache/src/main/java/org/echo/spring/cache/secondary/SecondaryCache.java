package org.echo.spring.cache.secondary;

import org.springframework.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.echo.spring.cache.message.CacheMessage;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两级缓存
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class SecondaryCache extends AbstractValueAdaptingCache {

    private String name;

    private Cache cacheL1;

    /** 是否启动二级缓存,默认值不启用 **/
    private boolean l2Enabled;

    private Cache cacheL2;

    private String l2Topic;

    private CacheMessagePusher messagePusher;

    public SecondaryCache(String name, Cache cacheL1, Cache cacheL2,
                          SecondaryCacheProperties cacheProperties,
                          CacheMessagePusher messagePusher) {
        super(cacheProperties.isCacheNullValues());
        this.name = name;
        this.cacheL1 = cacheL1;
        this.cacheL2 = cacheL2;
        this.l2Enabled = cacheProperties.isLevel2Enabled();
        this.l2Topic = cacheProperties.getLevel2Topic();
        this.messagePusher = messagePusher;
    }

    @Override
    protected Object lookup(Object key) {
        log.debug("Lookup Cache of key {}",key);

        Object value = cacheL1.get(key);
        if(value != null) {
            log.debug("Get cache from cacheL1, the key is : {}", key);
            return value;
        }

        if(!l2Enabled)
            return null;

        value = getFromLevel2(key, null);

        if(value != null) {
            log.debug("Get cache from l2 and put it into l1, the key is : {}", key);
            this.cacheL1.put(key, value);
        }
        return value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.cacheL1;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        log.debug("Get Cache of key {}",key);

        Object value = lookup(key);
        if(value != null) {
            return (T) value;
        }

        if(!l2Enabled)
            return null;

        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();
            value = lookup(key);
            if(value != null) {
                return (T) value;
            }
            value = valueLoader.call();
            Object storeValue = toStoreValue(value);
            put(key, storeValue);
            return (T) value;
        } catch (Exception e) {
            log.error(ThrowableToString.toString(e));
        } finally {
            lock.unlock();
        }

        throw new IllegalStateException(key + "");
    }

    @Override
    public void put(Object key, Object value) {
        log.debug("Put cache of {}->{}",key,value);

        if (!super.isAllowNullValues() && value == null) {
            this.evict(key);
            return;
        }

        putToLevel2(key,value);
        cacheL1.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        log.debug("Put cache if absent of {}->{}",key,value);

        Object prevValue = null;
        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();
            prevValue = getFromLevel2(key,value);
            if(prevValue == null){
                putToLevel2(key,value);
                this.cacheL1.put(key,toStoreValue(value));
            }
        }catch (Exception e){
            log.error(ThrowableToString.toString(e));
        } finally {
            lock.unlock();
        }

        return toValueWrapper(prevValue);
    }

    @Override
    public void evict(Object key) {
        log.debug("Evict cache {}",key);

        if(this.l2Enabled) {
            this.cacheL2.evict(key);
            push(new CacheMessage(this.name, key));
        }
        this.cacheL1.evict(key);
    }

    @Override
    public void clear() {
        this.cacheL1.clear();
        if(this.l2Enabled){
            this.cacheL2.clear();
        }
    }

    private Object getFromLevel2(Object key,Object value){
        if(this.l2Enabled){
            return this.cacheL2.get(key, () -> value);
        }
        return null;
    }

    private void putToLevel2(Object key, Object value){
        if(this.l2Enabled){
            cacheL2.put(key,value);
            push(new CacheMessage(this.name, key));
        }
    }

    /**
     * 缓存变更时通知其他节点清理一级缓存
     * @param message a message to Redis server
     */
    private void push(CacheMessage message) {
        if(message != null)
            this.messagePusher.push(l2Topic, message);
    }
}