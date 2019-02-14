package org.echo.ddd.support.domain.model.vo;

import org.echo.util.ClassUtils;

import java.util.List;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class LabelValueObjectRepositoryFactory {

    private List<LabelValueObjectRepository> repositories;

    public LabelValueObjectRepositoryFactory(List<LabelValueObjectRepository> repositories) {
        this.repositories = repositories;
    }

    public LabelValueObjectRepository get(Class<? extends  LabelValueObject> labelValueObjectClass){
        for(LabelValueObjectRepository repository:repositories){
            boolean supported = ClassUtils.isParameterizedTypeOf(repository.getClass(), labelValueObjectClass);
            if(supported){
                return repository;
            }
        }
        return null;
    }
}