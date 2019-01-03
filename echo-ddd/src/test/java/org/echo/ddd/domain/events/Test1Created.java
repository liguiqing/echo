package org.echo.ddd.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
@Getter
public class Test1Created extends DomainEvent{

    private LocalDate now;

}