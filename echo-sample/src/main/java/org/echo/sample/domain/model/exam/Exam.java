package org.echo.sample.domain.model.exam;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.echo.ddd.domain.entity.PersistenceEntityObject;
import org.echo.ddd.domain.events.EventHandlers;
import org.echo.ddd.domain.id.AssociationId;
import org.echo.ddd.domain.id.AssociationIdConverter;
import org.echo.ddd.domain.id.Identity;

import org.echo.ddd.support.domain.model.vo.LabelDetail;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 考试
 *
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of="examId",callSuper = false)
@ToString(of={"examId","scope"})
@Entity
@Table(name = "t_ps_Exam")
public class Exam extends PersistenceEntityObject {

    private ExamId examId;

    @Column(name="projectId")
    @Convert(converter = AssociationIdConverter.class)
    private AssociationId project;

    private LocalDateTime createTime;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    @Type(type = "org.echo.hibernate.converter.EnumFieldConverter")
    private ExamScope scope;

    @Embedded
    private ExamCategory category;

    public Exam(ExamId examId, LabelDetail category) {
        super(examId);
        this.examId = examId;
        this.createTime = LocalDateTime.now();
        this.dateFrom = LocalDate.now();
        this.scope = ExamScope.School;
        this.category = new ExamCategory(category);
        EventHandlers.getInstance().post(new ExamCreated(this.examId));
    }

    public void joinProject(Identity projectId){
        this.joinProject(projectId.getId().toString());
    }

    public void joinProject(String projectId){
        this.project = new AssociationId(projectId);
    }

}