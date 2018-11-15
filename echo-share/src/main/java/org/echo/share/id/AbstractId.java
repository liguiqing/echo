package org.echo.share.id;

import lombok.*;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 *
 * @author Liguiqing
 * @since V1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract  class AbstractId<Id extends Serializable> implements Identity<Id> {

    private Id id;

}