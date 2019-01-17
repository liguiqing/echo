package org.echo.hibernate.converter;

import org.echo.util.DateUtils;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author Liguiqing
 * @since V1.0
 */

public enum HibernateEnumDateBean implements HibernateEnum{

    Today(DateUtils.fromLocalDate(LocalDate.now())),Tomorrow(DateUtils.fromLocalDate(LocalDate.now().plusDays(1)));

    private Date value;

    HibernateEnumDateBean(Date value){
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public Date getValue() {
        return this.value;
    }
}