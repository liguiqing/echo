package org.echo.ddd.domain.id;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class IdPrefixGeneratorNotFoundException extends RuntimeException {

    public IdPrefixGeneratorNotFoundException(){
        super("Can not found IdPrefixGenerator");
    }

    public IdPrefixGeneratorNotFoundException(String identiyIdClassName){
        super(String.format("Can not found IdPrefixGenerator of %s",identiyIdClassName));
    }
}