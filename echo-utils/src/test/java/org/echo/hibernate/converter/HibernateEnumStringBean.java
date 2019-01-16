package org.echo.hibernate.converter;

import java.time.LocalDate;

/**
 * @author Liguiqing
 * @since V1.0
 */

public enum HibernateEnumStringBean implements HibernateEnum<String>{

    Today(LocalDate.now().toString()),Tomorrow(LocalDate.now().plusDays(1).toString());

    private String value;

    HibernateEnumStringBean(String value){
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.value;
    }
}