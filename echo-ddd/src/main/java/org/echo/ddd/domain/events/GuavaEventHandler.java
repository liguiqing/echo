package org.echo.ddd.domain.events;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

/**
 * 基于Guava Event Handler
 *
 * @author Liguiqing
 * @since V1.0
 */

public abstract class GuavaEventHandler<E extends DomainEvent> implements DomainEventHandler<E>  {

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void on(E event) {
        when(event);
    }

    protected abstract void when(E event);
}