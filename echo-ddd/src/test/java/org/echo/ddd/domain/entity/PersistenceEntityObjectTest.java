package org.echo.ddd.domain.entity;

import org.echo.ddd.domain.id.AssociationId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : PersistenceEntityObject Test")
class PersistenceEntityObjectTest {

    @Test
    void test(){
        PersistenceEntityObject pe = new PersistenceEntityObject(){};
        pe.setTid(1l);
        PersistenceEntityObject pe2 = new PersistenceEntityObject(){};
        pe2.setTid(1l);
        assertEquals(pe,pe2);
        pe2.setTid(11l);
        assertNotEquals(pe,pe2);

        assertEquals(new PersistenceEntityObject(){},new PersistenceEntityObject(){});
        assertEquals(new PersistenceEntityObject(new AssociationId("123")){}, new PersistenceEntityObject(new AssociationId("123")){});
        PersistenceEntityObject eo = new PersistenceEntityObject(new AssociationId("123")){};
        eo.setTid(1L);
        assertEquals(new AssociationId("123"),eo.getId());
        assertTrue(eo.getTid().compareTo(1L) == 0);
        assertEquals("EntityObject(id=AssociationId(id=123))",eo.toString());
        assertFalse(eo.canEqual(""));
        assertTrue(eo.canEqual(eo));
        assertTrue(eo.equals(eo));
        assertFalse(eo.equals(""));
        HashSet set = new HashSet();
        set.add(eo);
        set.add(eo);
        assertTrue(set.size() == 1);

        PersistenceEntityObject eo2 = new PersistenceEntityObject(){};
        PersistenceEntityObject eo3 = new PersistenceEntityObject(new AssociationId("123")){};
        PersistenceEntityObject eo4 = new PersistenceEntityObject(){};
        assertFalse(eo2.equals(eo3));
        assertFalse(eo3.equals(eo4));
    }
}