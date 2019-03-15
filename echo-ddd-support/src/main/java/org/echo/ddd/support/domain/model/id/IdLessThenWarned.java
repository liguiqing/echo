package org.echo.ddd.support.domain.model.id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.echo.ddd.domain.events.DomainEvent;

/**
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode(exclude = "prefixBean",callSuper = true)
public class IdLessThenWarned extends DomainEvent {
    private IdPrefixBean prefixBean;
}