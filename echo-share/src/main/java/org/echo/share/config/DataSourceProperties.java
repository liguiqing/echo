package org.echo.share.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Liguiqing
 * @since V3.0
 */

@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jdbc")
public class DataSourceProperties {

    private String url;

    private String username;

    private String password;

    private String jndiName;

}