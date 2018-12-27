package org.echo.spring.cache.secondary;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.spring.cache.CustomizedCache;
import org.echo.spring.cache.message.CacheMessage;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.util.StringUtils;

import java.util.UUID;
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

    private String identifier = UUID.randomUUID().toString();

    private String name;

    private Cache cacheL1;

    private Cache cacheL2;

    private String topic;

    private CacheMessagePusher messagePusher;

    public SecondaryCache(boolean allowNullValues){
        super(allowNullValues);
    }

    public SecondaryCache(String name, Cache cacheL1, Cache cacheL2,
                          SecondaryCacheProperties cacheProperties,
                          CacheMessagePusher messagePusher) {
        this(cacheProperties.isCacheNullValues());
        this.name = name;
        this.cacheL1 = cacheL1;
        this.topic = cacheProperties.getCacheMessageTopic();
        this.messagePusher = messagePusher;
        if(cacheProperties.isLevel2Enabled()){
            this.cacheL2 = cacheL2;
        }
    }

    public static SecondaryCache onlyCache1(String name, Cache cacheL1,SecondaryCacheProperties cacheProperties){
        SecondaryCache cache = new SecondaryCache(cacheProperties.isCacheNullValues());
        cache.name = name;
        cache.cacheL1 = cacheL1;
        return cache;
    }

    public static SecondaryCache onlyCache2(String name, Cache cacheL2,SecondaryCacheProperties cacheProperties){
        SecondaryCache cache = new SecondaryCache(cacheProperties.isCacheNullValues());
        cache.name = name;
        cache.cacheL2 = cacheL2;
        return cache;
    }


    public String getIdentifier(){
        return this.identifier;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.cacheL1==null?this.cacheL2:this.cacheL1;
    }

    @Override
    protected Object lookup(Object key) {
        log.debug("Lookup Object from cache [{}] by key [{}]",this.name,key);

        Object value = getFromLevel1(key);
        if(value != null) {
            return value;
        }

        if(!hasCache2())
            return null;

        value = getFromLevel2(key, null);

        if(value != null) {
            putToLevel1(key, value);
        }
        return value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        log.debug("Get Object from cache [{}] by key [{}]",this.name,key);

        Object value = lookup(key);
        if(value != null) {
            return (T) value;
        }

        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();

            value = valueLoader.call();
            putToLevel1(key, value);
            putToLevel2(key,value);
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
        log.debug("Put cache of {}->{} into cache [{}]",key,value,this.name);

        if (!super.isAllowNullValues() && value == null) {
            //不允许为null时,put null 将清空缓存
            this.evict(key);
            return;
        }
        putToLevel1(key, value);
        putToLevel2(key,value);
        push(new CacheMessage(identifier,this.name, key));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        log.debug("Put cache if absent of {}->{}",key,value);

        if (!super.isAllowNullValues() && value == null){
            return null;
        }

        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();
            Object prevValue = getFromLevel2(key,value);
            putToLevel1(key, value);
            return toValueWrapper(prevValue);
        }catch (Exception e){
            log.error(ThrowableToString.toString(e));
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public void evict(Object key) {
        log.debug("Evict by key [{}] from cache [{}]",key,this.name);

        if(this.hasCache2()) {
            this.cacheL2.evict(key);
        }
        if(this.cacheL1 != null)
            this.cacheL1.evict(key);
        push(new CacheMessage(identifier,this.name, key));
    }

    @Override
    public void clear() {
        log.debug("Clear cache [{}]",this.name);
        if(hasCache1())
            this.cacheL1.clear();
        if(this.hasCache2()){
            this.cacheL2.clear();
        }
        push(new CacheMessage(identifier,this.name,null));
    }

    private Object getFromLevel1(Object key){
        if(!hasCache1())
            return null;

        Object value = cacheL1.get(key);
        if(value != null) {
            log.debug("Hits by key [{}] from level1 in cache [{}]", key,this.name);
            if(value instanceof ValueWrapper){
                return ((ValueWrapper)value).get();
            }
            return value;
        }

        log.debug("Don't hit by key [{}] from level1 in cache [{}]", key,this.name);
        return null;
    }

    private void putToLevel1(Object key,Object value){
        if(!hasCache1())
            return ;

        log.debug("Put cache by key {} into level1 of cache [{}]", key,this.name);
        this.cacheL1.put(key, value);
    }

    private Object getFromLevel2(Object key,Object value){
        if(this.hasCache2()){
            Object o;
            if(value == null)
                o =  this.cacheL2.get(key);
            else
                o =  this.cacheL2.get(key);

            if(o != null)
                log.debug("Hit by key [{}] from level2 in cache [{}]", key,this.name);
            else
                log.debug("Don't hit by key [{}] from level2 in cache [{}]", key,this.name);
            return (o instanceof ValueWrapper) ?((ValueWrapper)o).get():o;
        }
        return null;
    }

    private void putToLevel2(Object key, Object value){
        if(this.hasCache2()){
            log.debug("Put cache by key [{}] into level2 of cache [{}]", key,this.name);
            cacheL2.put(key,value);
        }
    }

    private boolean hasCache1(){
        return this.cacheL1 != null;
    }

    private boolean hasCache2(){
        return this.cacheL2 != null;
    }

    /**
     * 缓存变更时通知其他节点清理一级缓存
     * @param message a message to Redis server
     */
    private void push(CacheMessage message) {
        if(hasCache1() && this.messagePusher != null) {
            log.debug("Push a cache update message");
            this.messagePusher.push(topic, message);
        }
    }


    /**
     * 清理本地缓存
     * @param key key of cache what's be cleared; clean level1 if key is null
     */
    public void clearLocal(Object key) {
        if(key != null) {
            log.debug("Clear  level1 by key [{}] in cache [{}]", key,this.name);
            this.cacheL1.evict(key);
        }else{
            log.debug("Clear  level1 of cache [{}]", this.name);
            this.cacheL1.clear();
        }
    }

}