package org.echo.spring.cache.multi;

import org.springframework.cache.Cache;

/**
 * @author Liguiqing
 * @since V3.0
 */

public interface CacheIterator extends Cache {

    boolean hasNext();

    CacheIterator next();
}