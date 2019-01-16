package org.echo.ddd.domain.id;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : AssociationIdConverter Test")
class AssociationIdConverterTest {

    @Test
    void convertToDatabaseColumn() {
        AssociationIdConverter converter = new AssociationIdConverter();
        assertNull(converter.convertToDatabaseColumn(new AssociationId()));
        assertNotNull(converter.convertToDatabaseColumn(new AssociationId("AssociationId")));
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute() {
        AssociationIdConverter converter = new AssociationIdConverter();
        assertNull(converter.convertToEntityAttribute(null));
        assertNotNull(converter.convertToEntityAttribute(""));
        AssociationId id = converter.convertToEntityAttribute("123");
        assertEquals("123",id.getId());
        assertEquals("AssociationId(id=123)",id.toString());
        assertEquals(new AssociationId("1111"),new AssociationId("1111"));
        assertNotEquals(new AssociationId("1111a"),new AssociationId("1111"));
    }
}