package org.echo.ddd.domain.vo;

import lombok.Getter;
import org.echo.ddd.domain.IdentifiedDomainObject;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 具有标识的值对象(要进行资源管理)
 *
 * @author Liguiqing
 * @since V1.0
 */
@Getter
@MappedSuperclass
public abstract class IdentifiedValueObject extends ValueObject implements IdentifiedDomainObject {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long tid;
}