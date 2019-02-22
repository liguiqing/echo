package org.echo.sample.application.exam;

import lombok.Data;
import org.echo.ddd.domain.id.AssociationId;
import org.echo.ddd.support.domain.model.vo.LabelDetail;
import org.echo.ddd.support.domain.model.vo.LabelDetailService;
import org.echo.hibernate.converter.HibernateEnum;
import org.echo.sample.domain.model.exam.Exam;
import org.echo.sample.domain.model.exam.ExamCategory;
import org.echo.sample.domain.model.exam.ExamId;
import org.echo.sample.domain.model.exam.ExamScope;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Data
public class NewExamCommand{
    private String projectId;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private int scope;

    private int category;

    public Exam toExam(ExamId examId, LabelDetailService detailService){
        LabelDetail labelDetail = detailService.toLabelDetail(ExamCategory.class,String.valueOf(this.category));
        ExamScope scope = HibernateEnum.fromValue(ExamScope.class, this.scope);
        return new Exam(examId,new AssociationId(projectId),
                LocalDateTime.now(),
                this.dateFrom,
                this.dateTo,
                scope,new ExamCategory(labelDetail));
    }
}