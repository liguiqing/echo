package org.echo.ddd.support.infrastructure.id;

/**
 * @author Liguiqing
 * @since V1.0
 */

@FunctionalInterface
public interface PrefixExistCallback<R,P> {
    R callback(P prefix);
}