package org.echo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Getter
@Setter
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "test")
public class TestBean {

    private String  master;

    private Caffeine caffeine = new Caffeine();

    @Getter
    @Setter
    public class Caffeine{
        long expireAfterAccess = 1L;
    }
}