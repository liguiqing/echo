package org.echo.ddd.domain;


import org.echo.ddd.domain.id.Identity;
import org.springframework.data.repository.Repository;

/**
 * @author Liguiqing
 * @since V1.0
 */

public interface PersistenceDomainObjectRepository <E extends IdentifiedDomainObject,T extends Identity> extends Repository<E,T> {
    T nextIdentity();
}