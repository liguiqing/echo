package org.echo.commons.id;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.UUID;

/**
 * 唯一标识工厂
 *
 * @author Liguiqing
 * @since V1.0
 */
public class Identities {

    private static IdentityGenerator localGen = new IdentityGenerator() {
        @Override
        public Serializable genId() {
            return UUID.randomUUID().toString();
        }

        @Override
        public Serializable genId(Serializable prefix) {
            if(prefix instanceof String){
                return genStringId((String) prefix);
            }
            return genIntegerId((Number)prefix);
        }
    };

    private static IdentityGenerator generator;

    public static void setGenerator(IdentityGenerator gen){
        generator = gen;
    }

    private static IdentityGenerator getGen(){
        if (generator == null)
            return localGen;
        return generator;
    }

    public static Serializable genId(){
        return getGen().genId();
    }

    public static Serializable genId(Serializable prefix){
        return getGen().genId(prefix);
    }

    private static String genStringId(String prefix){
        String uuid = UUID.randomUUID().toString();
        return prefix + uuid;
    }

    private static BigInteger genIntegerId(Number prefix){
        //TODO
        String s = prefix + (UUID.randomUUID().toString().hashCode()+"");
        return BigInteger.valueOf(Long.valueOf(s));
    }
}