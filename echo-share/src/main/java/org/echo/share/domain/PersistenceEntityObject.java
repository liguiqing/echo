package org.echo.share.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.echo.share.id.Identity;


import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 可持久化的领域实体对象
 *
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class PersistenceEntityObject extends EntityObject implements PersistenceDomainObject {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Serializable tid;

    public PersistenceEntityObject(Identity id) {
        super(id);
    }
}