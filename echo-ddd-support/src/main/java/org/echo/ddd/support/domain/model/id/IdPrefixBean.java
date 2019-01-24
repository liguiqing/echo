package org.echo.ddd.support.domain.model.id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */

@NoArgsConstructor
@EqualsAndHashCode(of = {"idClassName"},callSuper = false)
@ToString(of = {"idClassName","idClassNameHash","idPrefix"})
@Getter
@Entity
@Table(name = "t_ddd_IdPrefix")
public class IdPrefixBean implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long tid;

    private int idClassNameHash;

    private String idClassName;

    private String idPrefix;

    private Long idSeq;

    public IdPrefixBean(String idClassName, String idPrefix, Long idSeq) {
        this.idClassNameHash = idClassName.hashCode();
        this.idClassName = idClassName;
        this.idPrefix = idPrefix;
        this.idSeq = idSeq;
    }

    public void add(long value) {
        idSeq = idSeq + value;
    }
}