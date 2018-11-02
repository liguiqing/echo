package org.echo.share.domain;

import lombok.*;
import org.echo.share.id.Identity;

/**
 * 领域实体对象
 *
 * 实体对象:在业务域中,具有唯一标识,且可以根据唯一标识追踪变化的对象
 *
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public abstract class EntityObject implements IdentifiedDomainObject{

    private Identity id;

}