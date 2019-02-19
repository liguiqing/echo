package org.echo.share.config;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.echo.util.DateUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName(" Echo FreemarkerStaticModels Test")
class FreemarkerStaticModelsTest {

    @Test
    void test(){
        assertTrue(true);
        Map<String, String> map = new HashMap<>();
        map.put("DateUtils", DateUtils.class.getName());
        FreemarkerStaticModels staticModels = new FreemarkerStaticModels(map);
        assertNotNull(staticModels.get("DateUtils"));
        PropertiesConfiguration propertiesConfig = new PropertiesConfiguration();
        propertiesConfig.addProperty("DateUtils",DateUtils.class.getName());
        staticModels = new FreemarkerStaticModels(propertiesConfig);
        assertNotNull(staticModels.get("DateUtils"));

        propertiesConfig = new PropertiesConfiguration();
        propertiesConfig.addProperty("DateUtils",StaticModelTests.class.getName());
        staticModels = new FreemarkerStaticModels(propertiesConfig);
        assertNull(staticModels.get("DateUtils"));

        staticModels = new FreemarkerStaticModels();
        Map<String,String> m = FreemarkerStaticModels.defaultStaticModels();
        Set<String> keys = m.keySet();
        for(String key:keys){
            assertNotNull(staticModels.get(key));
        }
    }
}