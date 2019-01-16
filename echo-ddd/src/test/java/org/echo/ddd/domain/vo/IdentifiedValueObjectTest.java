package org.echo.ddd.domain.vo;

import org.echo.ddd.domain.id.AssociationId;
import org.echo.ddd.domain.id.Identity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : IdentifiedValueObject Test")
class IdentifiedValueObjectTest {

    @Test
    void test(){
        IdentifiedValueObject ivo = new IdentifiedValueObject(){};
        assertEquals(ivo,new IdentifiedValueObject(){});
        assertNull(ivo.getId());
        Identity id =  new AssociationId("123");
        ivo = new IdentifiedValueObject(id){};
        assertEquals(ivo,new IdentifiedValueObject(id){});
        assertEquals(id,ivo.getId());
        assertEquals("IdentifiedValueObject(id=AssociationId(id=123))",ivo.toString());
    }
}