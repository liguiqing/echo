package org.echo.sample.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SampleApplication extends SpringBootServletInitializer {

    public static void main(String[] args){
        log.debug("TFK Boot Starting ...");
        SpringApplication app = new SpringApplication(SampleApplication.class);
        app.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        log.debug("Add Configurations ");
        application.sources(SampleApplication.class);
        return super.configure(application);
    }
}