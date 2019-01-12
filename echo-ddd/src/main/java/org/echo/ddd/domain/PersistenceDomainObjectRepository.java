package org.echo.ddd.domain;


import org.echo.ddd.domain.id.Identity;
import org.springframework.data.repository.Repository;

/**
 * @author Liguiqing
 * @since V1.0
 */
@FunctionalInterface
public interface PersistenceDomainObjectRepository <E extends IdentifiedDomainObject,T extends Identity> extends Repository<E,T> {
    T nextIdentity();
}