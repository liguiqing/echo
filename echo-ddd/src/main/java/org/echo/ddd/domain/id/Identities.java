package org.echo.ddd.domain.id;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;

import java.io.Serializable;

/**
 * 唯一标识工厂
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class Identities {

    private static IdentityGenerator generator = new IdentityGenerator<String,String>(){};

    public static void setGenerator(IdentityGenerator generator){
        Identities.generator = generator;
    }

    public static <T extends Serializable> T genId(){
        return (T)generator.genId();
    }

    public static <T extends Serializable,P extends Serializable> T genId(P prefix){
        return (T)generator.genId(prefix);
    }

    public static <T extends Identity,P extends Serializable> T genId(P prefix,Class<T> clazz){
        try {
            Identity id = clazz.newInstance();
            id.setId(generator.genId(prefix));
            return (T)id;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(ThrowableToString.toString(e));
            throw new IllegalArgumentException(e);
        }
    }
}