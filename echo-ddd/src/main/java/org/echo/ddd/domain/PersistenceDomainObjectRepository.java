package org.echo.ddd.domain;


import org.echo.ddd.domain.id.Identity;
import org.springframework.data.repository.Repository;

/**
 * 领域对象持久化
 *
 * @author Liguiqing
 * @since V1.0
 */
@FunctionalInterface
public interface PersistenceDomainObjectRepository <E extends IdentifiedDomainObject,T extends Identity> extends Repository<E,T> {

    /**
     *  读取新的唯一标识
     *
     * @return SubClazz of {@link Identity}
     */
    T nextIdentity();
}