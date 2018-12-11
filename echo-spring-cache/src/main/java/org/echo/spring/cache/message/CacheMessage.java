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

    /*****发送方标识****/
    private String identifier;

    /** 待处理的缓存name **/
    private String cacheName;

    /** 待处理的缓存key **/
    private Object key;

    public boolean sameOfIdentifier(String identifier){
        return this.identifier.equals(identifier);
    }
}