package org.echo.commons.id;

import java.io.Serializable;

/**
 * 唯一标识
 *
 * @author Liguiqing
 * @since V1.0
 */

public interface Identity extends Serializable {

    Serializable getId();
}