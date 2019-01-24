package org.echo.ddd.domain.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

@DisplayName("Echo : DomainEvent Test")
class DomainEventTest {

    @Test
    void test(){
        assertTrue(true);

        DomainEvent e1 = new DomainEvent(){};
        DomainEvent e2 = new DomainEvent() {};
        assertNotEquals(e1,e2);
        assertNotEquals(e1.getEventId(),e2.getEventId());
        assertNotNull(e1.getOccured());
    }
}