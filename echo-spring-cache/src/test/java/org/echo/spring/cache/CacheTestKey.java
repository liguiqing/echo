package org.echo.spring.cache;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Liguiqing
 * @since V3.0
 */

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class CacheTestKey implements Serializable {
    private String key;

}