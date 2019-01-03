package org.echo.ddd.domain.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 领域事件
 *
 * @author Liguiqing
 * @since V1.0
 */
@Getter
@EqualsAndHashCode(of = {"eventId"})
@ToString
public abstract class DomainEvent implements Serializable {
    private String eventId ;

    private LocalDateTime occured;

    public DomainEvent(){
        this.eventId = UUID.randomUUID().toString();
        this.occured = LocalDateTime.now();
    }

}