package org.echo.ddd.domain.id;

import java.io.Serializable;

/**
 * Id生成器
 *
 * @author Liguiqing
 * @since V1.0
 */

public interface IdentityGenerator<T extends Serializable,C extends Class<? extends Identity>> {

    T genId();

    T genId(C c);
}