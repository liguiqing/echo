package org.echo.ddd.support.domain.model.vo;

import lombok.*;
import org.echo.ddd.domain.vo.ValueObject;

import javax.persistence.*;

/**
 * 键值＋标签类型值对象
 *
 * 持久化于表cm_label_vo
 *
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"detail"},callSuper = false)
@ToString(of = {"detail"})
@Entity
@Table(name = "t_ddd_label_vo")
public class LabelValueObject extends ValueObject {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long tid;

    @Embedded
    private LabelDetail detail;

    public LabelValueObject(int seq, String label, String valueText, String category) {
        this.detail = new LabelDetail(seq, label, valueText, category);
    }

    public void exchangeSeq(LabelValueObject other){
        if(!this.detail.sameCategoryAs(other.detail))
            return;

        LabelDetail thisDetail = this.detail.copyWithNewSeq(other.getDetail().getSeq());
        LabelDetail otherDetail = other.detail.copyWithNewSeq(this.getDetail().getSeq());
        other.detail = otherDetail;
        this.detail = thisDetail;
    }

    public void newDetail(LabelDetail detail){
        if(this.detail.sameCategoryAs(detail))
            this.detail = detail;
    }

    protected boolean valueOf(String value) {
        return this.detail.sameValueAs(value);
    }
}