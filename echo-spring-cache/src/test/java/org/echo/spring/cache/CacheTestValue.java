package org.echo.spring.cache;

import com.google.common.collect.Sets;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Liguiqing
 * @since V3.0
 */
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(of = {"v1"})
@ToString(of = {"v1","v2","v3"})
public class CacheTestValue implements Serializable {
    private String v1;

    private Boolean v2;

    private int v3;

    private LocalDate v4;

    private Collection<CacheTestValue> children;

    public void addChild(CacheTestValue child){
        if(Objects.isNull(this.children)){
            this.children = Sets.newHashSet();
        }
        this.children.add(child);
    }
}