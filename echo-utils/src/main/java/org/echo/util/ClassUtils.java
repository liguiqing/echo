package org.echo.util;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class ClassUtils {

    private ClassUtils() {
        throw new AssertionError("No org.echo.util.ClassUtils instances for you!");
    }

    /**
     * 检查接口或者类上定义的泛型类型是否包含指定类型
     *
     * @param oClass                 待判断的接口或者类
     * @param parameterizedTypeClass 须包含的泛型类
     * @return 如果包含 parameterizedTypeClass 返回 true 否则返回 false
     */
    public static boolean isParameterizedTypeOf(Class oClass, Class parameterizedTypeClass) {
        log.debug("Test Object {} is ParameterizedType of {}", oClass.getName(), parameterizedTypeClass.getName());

        Type[] types = oClass.getGenericInterfaces();
        if (types.length == 0) {
            types = new Type[]{oClass.getGenericSuperclass()};
        }else{
            types = Arrays.copyOf(types,types.length + 1);
            types[types.length-1] = oClass.getGenericSuperclass();
        }

        for (Type type1 : types) {
            if (contains(type1, parameterizedTypeClass)) {
                return true;
            }
        }

        return false;
    }

    private static boolean contains(Type type, Class parameterizedTypeClass) {
        if (!(type instanceof ParameterizedType)) {
            return false;
        }

        Type[] genericTypes = ((ParameterizedType) type).getActualTypeArguments();
        for (Type t : genericTypes) {
            if (parameterizedTypeClass.equals(t)) {
                return true;
            }
        }
        return false;
    }

    public static Object invoke(Object o,String methodName,Object... args){
        Class[] argsClasses = null;
        if(args != null){
            argsClasses = new Class[args.length];
            int i = 0;
            for(Object arg:args){
                argsClasses[i++] = arg.getClass();
            }
        }
        Method method = BeanUtils.findMethod(o.getClass(), methodName, argsClasses);
        if(method == null)
            return null;
        method.setAccessible(true);
        try {
            return method.invoke(o, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(ThrowableToString.toString(e));
        }
        return null;
    }
}