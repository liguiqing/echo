package org.echo.commons.id;

import java.io.Serializable;

/**
 * Id生成器
 *
 * @author Liguiqing
 * @since V3.0
 */

public interface IdentityGenerator {

    Serializable genId();

    Serializable genId(Serializable prefix);
}