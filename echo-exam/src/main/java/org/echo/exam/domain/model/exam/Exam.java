package org.echo.exam.domain.model.exam;

import lombok.NoArgsConstructor;
import org.echo.share.domain.PersistenceEntityObject;
import org.echo.share.id.commons.ExamId;

import javax.persistence.*;

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