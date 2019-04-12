package org.echo.ddd.domain.vo;

import org.echo.ddd.domain.IdentifiedDomainObject;

import javax.persistence.MappedSuperclass;

/**
 * 具有标识的值对象(要进行资源管理)
 *
 * @author Liguiqing
 * @since V1.0
 */
@MappedSuperclass
public abstract class IdentifiedValueObject extends PersistenceValueObject implements IdentifiedDomainObject {

}