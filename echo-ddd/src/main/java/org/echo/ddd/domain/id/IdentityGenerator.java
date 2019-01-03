package org.echo.ddd.domain.id;

import java.io.Serializable;
import java.util.UUID;

/**
 * Id生成器
 *
 * @author Liguiqing
 * @since V3.0
 */

public interface IdentityGenerator<Id extends Serializable,P extends Serializable> {

    default Id genId(){
        return (Id)UUID.randomUUID().toString().replaceAll("-","");
    }

    default Id genId(P prefix){
        if(prefix == null)
            return this.genId();
        if(prefix instanceof String){
            return (Id)prefix.toString().concat(this.genId().toString());
        }
        //TODO
        return genId();
    }
}