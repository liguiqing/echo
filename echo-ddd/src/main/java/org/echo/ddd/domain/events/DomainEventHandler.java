package org.echo.ddd.domain.events;

import java.io.Serializable;

/**
 * 领域事件处理接口
 *
 * @author Liguiqing
 * @since V1.0
 */
@FunctionalInterface
public interface DomainEventHandler <E extends DomainEvent> extends Serializable {

    void on(E event);
}