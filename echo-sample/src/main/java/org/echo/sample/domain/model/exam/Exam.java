package org.echo.sample.domain.model.exam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.echo.ddd.domain.entity.PersistenceEntityObject;
import org.echo.ddd.domain.events.EventHandlers;
import org.echo.share.id.commons.ExamId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 考试
 *
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "t_ps_Exam")
public class Exam extends PersistenceEntityObject {

    @Column
    private ExamId examId;

    @Column
    private LocalDateTime createTime;

    public Exam(ExamId examId) {
        super(examId);
        this.examId = examId;
        this.createTime = LocalDateTime.now();
        EventHandlers.getInstance().post(new ExamCreated(this.examId));
    }

}