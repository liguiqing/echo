package org.echo.ddd.infrastructure.id;

/**
 * @author Liguiqing
 * @since V1.0
 */

@FunctionalInterface
public interface PrefixExistCallback<R,P> {
    R callback(P prefix);
}