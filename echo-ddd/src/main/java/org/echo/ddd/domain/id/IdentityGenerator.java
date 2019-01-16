package org.echo.ddd.domain.id;

import java.io.Serializable;

/**
 * Id生成器
 *
 * @author Liguiqing
 * @since V1.0
 */

public interface IdentityGenerator<T extends Serializable,P extends T> {

    T genId();

    T genId(P prefix);
}