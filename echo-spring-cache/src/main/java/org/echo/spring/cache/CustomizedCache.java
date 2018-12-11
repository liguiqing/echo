package org.echo.spring.cache;

import org.springframework.cache.Cache;

/**
 * 扩展spring cache
 *
 * @author Liguiqing
 * @since V1.0
 */

public interface CustomizedCache extends Cache {
    void refresh(Object key);
}