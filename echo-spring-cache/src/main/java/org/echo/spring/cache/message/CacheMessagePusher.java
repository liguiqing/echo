package org.echo.spring.cache.message;

/**
 * @author Liguiqing
 * @since V3.0
 */

public interface CacheMessagePusher {

    void push(String topic, CacheMessage message);
}