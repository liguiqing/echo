package org.echo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "test")
public class TestBean {

    private String  master;

    private Caffeine caffeine = new Caffeine();

    @Getter
    @Setter
    public class Caffeine{
        long expireAfterAccess;
    }
}