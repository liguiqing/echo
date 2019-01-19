package org.echo.ddd.support.domain.model.id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.echo.ddd.domain.events.DomainEvent;
import org.echo.ddd.support.infrastructure.id.CachingStringIdentityGenerator;

import java.util.Deque;

/**
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
@Getter
public class IdLessThenWarned extends DomainEvent {

    private  CachingStringIdentityGenerator identityGenerator;

    private IdPrefixBean prefixBean;

    private Deque<Long> deque;
}