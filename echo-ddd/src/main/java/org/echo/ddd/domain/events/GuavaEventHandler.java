package org.echo.ddd.domain.events;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.echo.util.ClassUtils;

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
        if(support(event))
            when(event);
    }

    protected abstract void when(E event);

    protected boolean support(E event){
        return ClassUtils.isParameterizedTypeOf(this.getClass(),event.getClass());
    }
}