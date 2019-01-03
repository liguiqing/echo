package org.echo.sample.domain.model.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.echo.ddd.domain.events.DomainEvent;
import org.echo.share.id.commons.ExamId;

/**
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
@Getter
public class ExamCreated extends DomainEvent {
    private ExamId examId;

}