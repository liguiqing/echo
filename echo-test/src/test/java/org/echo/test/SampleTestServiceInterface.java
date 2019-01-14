package org.echo.test;

import java.util.Collection;

/**
 * @author Liguiqing
 * @since V3.0
 */

public interface SampleTestServiceInterface<T,K> {
    void doSomething(T t);

    T getSomething(K k);

    Collection<T> findSometingAll();
}