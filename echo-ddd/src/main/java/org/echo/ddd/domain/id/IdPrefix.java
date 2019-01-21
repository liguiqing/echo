package org.echo.ddd.domain.id;

/**
 * ID前缀
 *
 * @author Liguiqing
 * @since V1.0
 */

@FunctionalInterface
public interface IdPrefix<C extends Class<? extends Identity>> {
    /**
     *
     * @param c Class type Of {@link Identity}
     * @return Identity prefix 不会返回任何异常，处理不了，返回值为：""

     */
    String of(C c);
}