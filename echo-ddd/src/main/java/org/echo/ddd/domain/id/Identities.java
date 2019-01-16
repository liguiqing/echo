package org.echo.ddd.domain.id;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;

import java.io.Serializable;
import java.util.UUID;

/**
 * 唯一标识工厂
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class Identities {

    private Identities(){
        throw new AssertionError("No org.echo.ddd.domain.id.Identities instances for you!");
    }

    private static IdentityGenerator generator = new IdentityGenerator<String,String>(){

        @Override
        public String genId() {
            return UUID.randomUUID().toString().replaceAll("-","");
        }

        @Override
        public String genId(String prefix) {
            if(prefix == null)
                return this.genId();
            return prefix.concat(this.genId());
        }
    };

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
        } catch (Exception e) {
            log.error(ThrowableToString.toString(e));
            throw new IllegalArgumentException(e);
        }
    }
}