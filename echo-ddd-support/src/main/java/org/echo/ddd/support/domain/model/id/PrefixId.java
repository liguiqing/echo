package org.echo.ddd.support.domain.model.id;

import lombok.*;
import org.echo.ddd.domain.id.Identity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Liguiqing
 * @since V3.0
 */
@Embeddable
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@ToString
public class PrefixId implements Identity<Integer> {
    @Column(name = "idClassNameHash")
    private Integer id;
}