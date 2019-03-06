package org.echo.spring.cache.config;

import lombok.Getter;

import java.util.UUID;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class TestKeyGenerator {
    @Getter
    private String key = UUID.randomUUID().toString();

}