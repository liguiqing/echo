package org.echo.ddd.domain.id;

import java.io.Serializable;
import java.util.UUID;

/**
 * 唯一标识工厂
 *
 * @author Liguiqing
 * @since V1.0
 */
public class Identities {

    private static IdentityGenerator generator = new IdentityGenerator<String,String>(){};

    public static void setGenerator(IdentityGenerator generator){
        Identities.generator = generator;
    }

    public static <Id extends Serializable> Id genId(){
        return (Id)generator.genId();
    }

    public static <Id extends Serializable,P extends Serializable> Id genId(P prefix){
        return (Id)generator.genId(prefix);
    }

}