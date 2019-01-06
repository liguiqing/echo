package org.echo.ddd.domain.id;

import javax.persistence.AttributeConverter;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */

public class AssociationIdConverter implements AttributeConverter<AssociationId,String> {


    @Override
    public String convertToDatabaseColumn(AssociationId associationId) {
        if(associationId == null)
            return null;

        return associationId.getId();
    }

    @Override
    public AssociationId convertToEntityAttribute(String sId) {
        if(sId == null)
            return null;

        return new AssociationId(sId);
    }
}