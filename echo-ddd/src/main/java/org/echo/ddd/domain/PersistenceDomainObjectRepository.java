package org.echo.ddd.domain;


import org.echo.ddd.domain.id.Identity;
import org.springframework.data.repository.Repository;

/**
 * @author Liguiqing
 * @since V1.0
 */

public interface PersistenceDomainObjectRepository <T extends IdentifiedDomainObject,ID extends Identity> extends Repository<T,ID> {
    ID nextIdentity();

}