package org.echo.ddd.domain.id;

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

    private static IdentityGenerator generator;

    public static void setGenerator(IdentityGenerator gen){
        generator = gen;
    }

    private static <Id extends Serializable,P extends Serializable> Id genAId(P prefix){
        if(generator != null)
            return (Id)generator.genId(prefix);

        if(prefix == null)
            return (Id)genStringId(null);
        if(prefix instanceof String)
            return (Id)genStringId((String)prefix);

        return (Id) genLongId((Number)prefix);
    }

    public static <Id extends Serializable> Id genId(){
        return genAId(null);
    }

    public static <Id extends Serializable,P extends Serializable> Id genId(P prefix){
        return genAId(prefix);
    }

    private static String genStringId(String prefix){
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        if(prefix == null)
            return uuid;
        return prefix + uuid;
    }

    private static Long genLongId(Number prefix){
        //TODO
        String s = prefix + (UUID.randomUUID().toString().hashCode()+"");
        return Long.MAX_VALUE;
    }
}