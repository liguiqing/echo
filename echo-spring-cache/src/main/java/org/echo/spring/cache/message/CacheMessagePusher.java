package org.echo.spring.cache.message;

/**
 * 缓存变更消息发布者
 *
 * @author Liguiqing
 * @since V1.0
 */
@FunctionalInterface
public interface CacheMessagePusher {

    void push(String topic, CacheMessage message);
}