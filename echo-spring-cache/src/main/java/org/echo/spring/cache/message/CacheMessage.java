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

    /** 关闭/打开缓存级别:0< 打开;0>关闭;  -9打开所有;9关闭所有**/
    private int closedLevel;

    public CacheMessage(String identifier, String cacheName, Object key) {
        this.identifier = identifier;
        this.cacheName = cacheName;
        this.key = key;
        this.closedLevel = 0;
    }

    public boolean isClosed(){
        return this.closedLevel > 0 ;
    }

    public boolean isOpen(){
        return this.closedLevel < 0;
    }

    public boolean sameOfIdentifier(String identifier){
        return this.identifier.equals(identifier);
    }
}