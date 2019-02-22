package org.echo.ddd.support.domain.model.vo;

import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author Liguiqing
 * @since V1.0
 */

@AllArgsConstructor
public class LabelDetailService {

    private LabelValueObjectRepository repository;

    public LabelDetail toLabelDetail(Class c, String value){
        List<LabelValueObject> lvos = this.repository.findAllByCategoryOrderBySeq(c.getName());
        for(LabelValueObject lvo:lvos){
            if(lvo.valueOf(value)){
                return lvo.getDetail();
            }
        }
        return null;
    }
}