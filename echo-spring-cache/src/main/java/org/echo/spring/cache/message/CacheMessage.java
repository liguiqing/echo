package org.echo.spring.cache.message;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 缓存变更消息
 *
 * @author Liguiqing
 * @since V1.0
 */

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class CacheMessage implements Serializable {

    private String cacheName;

    /** 待清理的缓存key;如果为空,清理cacheName缓存 **/
    private Object key;
}