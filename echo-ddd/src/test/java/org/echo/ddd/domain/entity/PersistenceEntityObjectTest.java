package org.echo.ddd.domain.entity;

import org.echo.ddd.domain.id.AssociationId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : PersistenceEntityObject Test")
class PersistenceEntityObjectTest {

    @Test
    void test(){
        assertEquals(new PersistenceEntityObject(){},new PersistenceEntityObject(){});
        assertEquals(new PersistenceEntityObject(new AssociationId("123")){}, new PersistenceEntityObject(new AssociationId("123")){});
        PersistenceEntityObject eo = new PersistenceEntityObject(new AssociationId("123")){};
        eo.setTid(1L);
        assertEquals(eo.getId(),new AssociationId("123"));
        assertTrue(eo.getTid().compareTo(1L) == 0);
        assertEquals("EntityObject(id=AssociationId(id=123))",eo.toString());
    }
}