package org.echo.ddd.domain.entity;

import lombok.Getter;
import org.echo.ddd.domain.IdentifiedDomainObject;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


/**
 * 领域实体对象
 *
 * 实体对象:在业务域中,具有唯一标识,且可以根据唯一标识追踪变化的对象
 *
 * @author Liguiqing
 * @since V1.0
 */
@Getter
@MappedSuperclass
public abstract class EntityObject implements IdentifiedDomainObject {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long tid;
}