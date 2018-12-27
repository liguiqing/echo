package org.echo.spring.cache.caffeine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class CaffeineProperties {

    /** 缓存名称 */
    private String name;

    /** 是否存储空值，默认true，防止缓存穿透*/
    private boolean cacheNullValues = true;

    /** 访问后过期时间，单位毫秒*/
    private long expireAfterAccess = 36000;

    /** 写入后过期时间，单位毫秒*/
    private long expireAfterWrite = 36000;

    /** 写入后刷新时间，单位毫秒*/
    private long refreshAfterWrite = 36000;

    /** 初始化大小*/
    private int initialCapacity = 500;

    /** 最大缓存对象个数，超过此数量时之前放入的缓存将失效*/
    private long maximumSize =5000;
}