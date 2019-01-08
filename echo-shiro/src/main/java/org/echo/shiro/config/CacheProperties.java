package org.echo.shiro.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Liguiqing
 * @since V3.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CacheProperties {
    private String name;

    private long maxIdleSecond = 12000;

}