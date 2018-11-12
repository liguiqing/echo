package org.echo.spring.cache.secondary;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CacheTestBean implements Serializable {
    private String f1;

    private int f2;

    private boolean f3;

    private LocalDateTime f4;

}