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

    private static IdPrefix<Class<? extends Identity>> idPrefix =(c)->c.getName().substring(0,4).toUpperCase();

    private static IdentityGenerator generator = new IdentityGenerator<String,Class<Identity>>(){

        @Override
        public String genId() {
            return UUID.randomUUID().toString().replaceAll("-","");
        }

        @Override
        public String genId(Class<Identity> c) {
            String prefix = idPrefix.of(c);
            if(prefix == null)
                return this.genId();
            return prefix.concat(this.genId());
        }
    };

    public static void setGenerator(IdentityGenerator generator){
        Identities.generator = generator;
    }

    public static void setIdPrefix(IdPrefix<Class<? extends Identity>> idPrefix){
        Identities.idPrefix = idPrefix;
    }

    public static <T extends Serializable> T genId(){
        return (T)generator.genId();
    }

    public static <T extends Serializable,C extends Class<? extends Identity>> T genId(C c){
        return (T)generator.genId(c);
    }

    public static <T extends Identity> T genId(T t){
        try {
            Identity id = t.getClass().newInstance();
            id.setId(generator.genId(id.getClass()));
            return (T)id;
        } catch (Exception e) {
            log.error(ThrowableToString.toString(e));
            throw new IllegalArgumentException(e);
        }
    }
}