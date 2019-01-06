package org.echo.ddd.domain.id;

import java.io.Serializable;
import java.util.UUID;

/**
 * Id生成器
 *
 * @author Liguiqing
 * @since V1.0
 */

public interface IdentityGenerator<T extends Serializable,P extends Serializable> {

    default T genId(){
        return (T)UUID.randomUUID().toString().replaceAll("-","");
    }

    default T genId(P prefix){
        if(prefix == null)
            return this.genId();
        if(prefix instanceof String){
            return (T)prefix.toString().concat(this.genId().toString());
        }
        return genId();
    }
}