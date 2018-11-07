package org.echo.spring.cache.redis;

import com.google.common.collect.Maps;
import lombok.*;

import java.util.Map;

/**
 * Redis Cache Control Bean
 *  二级缓存
 *
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Redis {

    /** 全局过期时间，单位毫秒，默认0,永不过期*/
    private long defaultExpiration = 0;

    /** cacheName的过期时间，单位毫秒，优先级比defaultExpiration高*/
    private Map<String, Long> expires = Maps.newHashMap();

    /** 缓存更新时通知其他节点的topic名称*/
    private String topic = "cache:redis:caffeine:topic";

}