package org.echo.spring.cache.secondary;

import lombok.*;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@EqualsAndHashCode(of="f1")
@ToString
public class CacheTestBean implements Serializable {
    private String f1;

    private int f2;

    private boolean f3;

    private LocalDateTime f4;

}