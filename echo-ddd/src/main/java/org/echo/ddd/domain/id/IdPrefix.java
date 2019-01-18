package org.echo.ddd.domain.id;

import com.sun.istack.internal.NotNull;

/**
 * ID前缀
 *
 * @author Liguiqing
 * @since V1.0
 */

@FunctionalInterface
public interface IdPrefix<C extends Class<? extends Identity>> {
    String of(@NotNull C c);
}