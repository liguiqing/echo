package org.echo.sample.domain.model.project;

import lombok.*;
import org.echo.ddd.domain.id.IdPrefix;
import org.echo.ddd.domain.id.Identities;
import org.echo.ddd.domain.id.Identity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Embeddable
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@ToString
public class ProjectId implements Identity<String> {
    @Column(name = "examId")
    private String id;

    public ProjectId() {
        this.id = Identities.genId(IdPrefix.ExamId);
    }
}