package org.echo.share.id.commons;

import lombok.*;
import org.echo.ddd.domain.id.Identities;
import org.echo.ddd.domain.id.Identity;
import org.echo.share.id.IdPrefix;


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Exam对象唯一标识
 *
 * @author Liguiqing
 * @since V3.0
 */
@Embeddable
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@ToString
public class ExamId implements Identity<String> {
    @Column(name = "examId")
    private String id;

    public ExamId() {
        this.id = Identities.genId(IdPrefix.ExamId);
    }
}