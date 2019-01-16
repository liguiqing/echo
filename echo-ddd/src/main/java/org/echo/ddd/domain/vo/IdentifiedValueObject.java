package org.echo.ddd.domain.vo;

import lombok.*;
import org.echo.ddd.domain.IdentifiedDomainObject;
import org.echo.ddd.domain.id.Identity;

/**
 * 具有标识的值对象(要进行资源管理)
 *
 * @author Liguiqing
 * @since V1.0
 */

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public abstract class IdentifiedValueObject extends ValueObject implements IdentifiedDomainObject {
    private Identity id;

}