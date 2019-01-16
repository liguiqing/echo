package org.echo.hibernate.converter;

/**
 * @author Liguiqing
 * @since V3.0
 */

public enum HibernateEnumIntegerBean implements HibernateEnum<Integer> {
    One(1),Two(2);

    private Integer value;

    HibernateEnumIntegerBean(Integer value){
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}