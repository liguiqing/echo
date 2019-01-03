package org.echo.ddd.domain.events;

/**
 * 领域事件处理工具
 *
 * @author Liguiqing
 * @since V1.0
 */

public class EventHandlers {

    private EventBus eventBus;

    private EventHandlers(){
        this.eventBus = new EventBus() {};
    }

    private final static class Holder {
        private final static EventHandlers instance = new EventHandlers();
    }

    public static EventHandlers getInstance() {
        return EventHandlers.Holder.instance;
    }

    public static void setEventBus(EventBus eventBus){
        getInstance().eventBus = eventBus;
    }

    public void register(DomainEventHandler eventHandler){
        this.eventBus.register(eventHandler);
    }

    public void unregister(DomainEventHandler eventHandler){
        this.eventBus.unregister(eventHandler);
    }

    public void post(DomainEvent event) {
        this.eventBus.post(event);
    }

}