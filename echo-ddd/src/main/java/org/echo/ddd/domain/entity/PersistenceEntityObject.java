package org.echo.ddd.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.echo.ddd.domain.id.Identity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

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
@EqualsAndHashCode(of = {"tid"})
public abstract class PersistenceEntityObject extends EntityObject {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long tid;

    public PersistenceEntityObject(Identity id) {
        super(id);
    }
}