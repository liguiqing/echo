package org.echo.sample.domain.model.exam;

import org.echo.hibernate.converter.HibernateEnum;

/**
 * 考试范围
 * 班级考试(Clazz)，年级考试(School)，多校联考(Union)，区域统考(Uniform),中考(High),高考(College)
 *
 * @author Liguiqing
 * @since V1.0
 */

public enum  ExamScope implements HibernateEnum<Integer> {

    Clazz("Clazz",1),School("School",2),Union("Union",3),Uniform("Uniform",4),High("High",5),College("College",6);

    private int value;

    private String key;

    ExamScope(String key,int value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return keyPrefix.concat(this.key);
    }

    @Override
    public Integer getValue() {
        return value;
    }

    private static final String keyPrefix = ExamScope.class.getName();
}