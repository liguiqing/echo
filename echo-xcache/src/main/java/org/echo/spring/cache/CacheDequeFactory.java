package org.echo.spring.cache;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 可用于缓存的　Deque
 * @author Liguiqing
 * @since V1.0
 */

public interface CacheDequeFactory {

    default Deque getDeque(String cacheName){
        return new ConcurrentLinkedDeque();
    }
}