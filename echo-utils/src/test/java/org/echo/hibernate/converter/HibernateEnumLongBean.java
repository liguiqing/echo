package org.echo.hibernate.converter;

/**
 * @author Liguiqing
 * @since V1.0
 */

public enum HibernateEnumLongBean implements HibernateEnum<Long>{

    One(1L),Two(2L);

    private Long value;

    HibernateEnumLongBean(Long value){
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public Long getValue() {
        return this.value;
    }
}