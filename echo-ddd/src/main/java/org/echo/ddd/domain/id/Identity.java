package org.echo.ddd.domain.id;

import java.io.Serializable;

/**
 * 唯一标识(并不一定是对象持久化时的主键)
 *
 * @author Liguiqing
 * @since V1.0
 */

public interface Identity<Id> extends Serializable {

    Id getId();

    void setId(Id id);
}