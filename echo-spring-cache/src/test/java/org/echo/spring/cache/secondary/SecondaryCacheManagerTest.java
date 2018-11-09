package org.echo.spring.cache.secondary;

import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Liguiqing
 * @since V1.0
 */
@ContextHierarchy(@ContextConfiguration(classes = {
        SecondaryCacheAutoConfiguration.class
}))
@DisplayName("Echo : SecondaryCacheManager test")
public class SecondaryCacheManagerTest extends AbstractConfigurationsTest {

    @Autowired
    private SecondaryCacheManager secondaryCacheManager;

    @Test
    public void test(){
        assertNotNull(secondaryCacheManager);
        Cache aCache = secondaryCacheManager.getCache("aCache");
        assertNotNull(aCache);
        Cache bCache = secondaryCacheManager.getCache("bCache");
        assertNotNull(bCache);
        String a = aCache.get("a", () -> "a");
        assertNotNull(a);
        aCache.clear();
    }

}