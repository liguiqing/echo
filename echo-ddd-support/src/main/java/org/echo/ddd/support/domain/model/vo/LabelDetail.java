package org.echo.ddd.support.domain.model.vo;

import lombok.*;
import org.echo.ddd.domain.vo.ValueObject;

import javax.persistence.Embeddable;

/**
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"valueText","category"},callSuper = false)
@ToString(of = {"label","valueText","category","seq"})
@Embeddable
public class LabelDetail extends ValueObject {

    private int seq;

    private String label;

    private String valueText;

    private String category;

    public LabelDetail copyWithNewSeq(int seq){
        return new LabelDetail(seq, this.label, this.valueText, this.category);
    }

    public boolean sameCategoryAs(LabelDetail other){
        return this.sameCategoryAs(other.category);
    }

    public boolean sameCategoryAs(String category){
        return this.category.equals(category);
    }

}