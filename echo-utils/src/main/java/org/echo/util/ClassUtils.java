package org.echo.util;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Objects;

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

    /**
     * 反射执行对象o {@link Object} 的methodName方法
     * @param o any {@link Object}
     * @param methodName of will be invoked
     * @param args {@link Class} of method parameter types
     * @return null if can'nt invoke else return method returns
     */
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

    /**
     * 查找o（{@link Object}）中满足 returnType 及 parameterTypes 的 Method
     * @param o any {@link Object}
     * @param returnType {@link Class} of method returns
     * @param parameterTypes {@link Class} of method parameter types
     * @return null if o is null or can't find any required method
     */
    public static Method findMethodOfReturns(Object o,Class<?> returnType,Class<?>... parameterTypes){
        if(Objects.isNull(o))
            return null;
        Class<?> cls = o.getClass();

        Method[] methods = cls.getDeclaredMethods();
        for(Method method:methods){
            if(method.getReturnType().equals(returnType)){
                try {
                    return cls.getMethod(method.getName(), parameterTypes);
                } catch (NoSuchMethodException e) {
                    log.error(ThrowableToString.toString(e));
                }
            }
        }

        return null;
    }

    /**
     * 通过Class 及 constructor 参数 实例化一个对象
     * @param cls of Constructor
     * @param parameterTypes {@link Class} of method parameter types
     * @param <T> return type
     * @return null if can'nt instance
     */
    public static <T> T newInstanceOf(Class cls,Object... parameterTypes){
        if(Objects.isNull(cls) || Objects.isNull(parameterTypes)){
            return null;
        }

        try {
            Class[] clses = new Class[parameterTypes.length];
            int i = 0;
            for(Object o:parameterTypes){
                clses[i++] = o.getClass();
            }
            return (T)cls.getDeclaredConstructor(clses).newInstance(parameterTypes);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error(ThrowableToString.toString(e));
        }
        return null;
    }
}