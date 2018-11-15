package org.echo.share.id;

import java.io.Serializable;

/**
 * Id生成器
 *
 * @author Liguiqing
 * @since V3.0
 */

public interface IdentityGenerator<Id extends Serializable,P extends Serializable> {

    Id genId();

    Id genId(P prefix);
}