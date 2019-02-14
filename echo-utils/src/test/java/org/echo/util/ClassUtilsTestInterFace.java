package org.echo.util;

import java.io.Serializable;

/**
 * @author Liguiqing
 * @since V1.0
 */

public interface ClassUtilsTestInterFace<T extends  ClassUtilsTestBean,K extends Serializable> {
    default void testMethod(){}
}