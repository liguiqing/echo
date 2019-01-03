package org.echo.ddd.domain.events;

import com.google.common.eventbus.AsyncEventBus;
import org.echo.util.Threads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Liguiqing
 * @since V1.0
 */
public interface EventBus {
    Logger log = LoggerFactory.getLogger(EventBus.class);

    AsyncEventBus bus = new AsyncEventBus(Threads.getExecutorService());

    default  void register(DomainEventHandler handler){
        log.debug("Register a event handler {}",handler);
        bus.register(handler);
    }

    default void unregister(DomainEventHandler handler){
        log.debug("Unregister a event handler {}",handler);
        bus.unregister(handler);
    }

    default void post(DomainEvent event){
        log.debug("Post a event {}",event);
        bus.post(event);
    }
}