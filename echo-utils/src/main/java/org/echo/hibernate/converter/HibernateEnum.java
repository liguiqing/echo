package org.echo.hibernate.converter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Hibernate 枚举转换
 *
 * @author Liguiqing
 * @since V1.0
 */

public interface HibernateEnum<V> extends Serializable {

    /**
     * 用于显示的枚举名时查询的键
     *
     * @return String
     */
    String getKey();

    /**
     * 存储到数据库的枚举值
     *
     * @return V
     */
    V getValue();

    //按枚举的value获取枚举实例
    static <T extends HibernateEnum> T fromValue(Class<T> enumType, Integer value) {
        for (T object : enumType.getEnumConstants()) {
            if (Objects.equals(value, object.getValue())) {
                return object;
            }
        }
        throw new IllegalArgumentException("No enum value " + value + " of " + enumType.getCanonicalName());
    }
}