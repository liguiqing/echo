package org.echo.spring.cache.redis;

import lombok.*;

/**
 * CaffeineProperties Cache Control Bean
 *
 * 一级缓存
 * @author Liguiqing
 * @since V3.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Caffeine {

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