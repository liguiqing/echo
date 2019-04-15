package org.echo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.echo.exception.ThrowableToString;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * 集合{@link Collection}工具
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class CollectionsUtil {

    private CollectionsUtil(){
        throw new AssertionError("No org.echo.util.CollectionsUtil instances for you!");
    }

    public static boolean isNotNullAndNotEmpty(Collection collection){
        return !isNullOrEmpty(collection);
    }

    public static boolean isNullOrEmpty(Collection collection){
        return (collection == null) || collection.isEmpty();
    }

    public static boolean hasElements(Collection collection){
        return isNotNullAndNotEmpty(collection);
    }

    public static <C extends Collection> void newCollectionIfNull(Object o, String field, Callable<C> callable){
        try{
            if (isFieldNull(o,field)) {
                FieldUtils.writeField(o,field,callable.call(),true);
            }
        }catch (Exception e){
            log.warn(ThrowableToString.toString(e));
        }
    }

    private static boolean isFieldNull(Object o,String field) throws IllegalAccessException {
        return Objects.isNull(FieldUtils.readField(o,field,true));
    }
}