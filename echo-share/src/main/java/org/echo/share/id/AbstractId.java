package org.echo.share.id;

import lombok.*;

import java.io.Serializable;

/**
 *
 * @author Liguiqing
 * @since V1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public abstract  class AbstractId implements Identity {

    private Serializable id;
}