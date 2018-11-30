package org.echo.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * 集合(java.util.Collection)工具
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class CollectionsUtil {

    public static <E> boolean isNotNullAndNotEmpty(Collection<E> collection){
        return !isNullOrEmpty(collection);
    }

    public static <E> boolean isNullOrEmpty(Collection<E> collection){
        return (collection == null) || collection.size() == 0;
    }

    public static <E> boolean hasElements(Collection<E> collection){
        return (collection != null) && collection.size() > 0;
    }

}