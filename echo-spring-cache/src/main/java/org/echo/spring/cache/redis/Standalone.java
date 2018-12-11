package org.echo.spring.cache.redis;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Liguiqing
 * @since V3.0
 */
@Setter
@Getter
public class Standalone {

    private String host;

    private int port;

    public String toUrl(){
        return "http://".concat(toHost());
    }

    public String toHost(){
        return host.concat(":").concat(port + "");
    }
}