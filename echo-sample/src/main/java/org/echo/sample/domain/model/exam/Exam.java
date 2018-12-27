package org.echo.sample.domain.model.exam;

import lombok.NoArgsConstructor;


import org.echo.ddd.domain.entity.PersistenceEntityObject;
import org.echo.share.id.commons.ExamId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 考试
 *
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@Entity
@Table(name = "t_ps_Exam")
public class Exam extends PersistenceEntityObject {


    @Column
    private ExamId examId;

    public Exam(ExamId examId) {
        super(examId);
        this.examId = examId;
    }

}