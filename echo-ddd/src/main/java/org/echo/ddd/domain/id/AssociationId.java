package org.echo.ddd.domain.id;

import lombok.*;

/**
 *关联ID,用于聚合间的关联
 *
 * @author Liguiqing
 * @since V1.0.0
 */

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@ToString
public  class AssociationId implements Identity<String> {
    private String id;
}