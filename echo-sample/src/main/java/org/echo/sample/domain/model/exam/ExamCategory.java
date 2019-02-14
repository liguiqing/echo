package org.echo.sample.domain.model.exam;

import lombok.*;
import org.echo.ddd.domain.vo.ValueObject;
import org.echo.ddd.support.domain.model.vo.LabelDetail;
import org.echo.util.NumbersUtil;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 考试类型
 *
 * @author Liguiqing
 * @since V3.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of={"category"},callSuper = false)
@ToString
@Embeddable
public class ExamCategory extends ValueObject {

    private int category;

    @Column(name="categoryText")
    private String label;

    public ExamCategory(LabelDetail from){
        if(from.sameCategoryAs(getClass().getName())){
            this.category = NumbersUtil.stringToInt(from.getValueText(), 1);
            this.label = from.getLabel();
        }
    }

}