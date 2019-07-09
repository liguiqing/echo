package org.echo.springboot.starter.fastjson;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.echo.springboot.starter.fastjson.FastJsonAutoConfiguration;
import org.echo.springboot.starter.fastjson.FastJsonProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <pre>
 *  FastJsonAutoConfiguration Test
 * </pre>
 *
 * @author liguiqing
 * @since V1.0.0 2019-07-05 15:37
 **/
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration( initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
        FastJsonAutoConfiguration.class
})
@DisplayName("FastJsonAutoConfiguration Test")
public class FastJsonAutoConfigurationTest {

    @Autowired
    private FastJsonJsonView fastJsonJsonView;

    @Autowired
    private FastJsonProperties fastJsonProperties;

    @Autowired
    private FastJsonAutoConfiguration fastJsonAutoConfiguration;

    @Test
    void test(){
        assertNotNull(fastJsonJsonView);
        fastJsonProperties.setSerializerFeatures(null);
        new FastJsonAutoConfiguration(this.fastJsonProperties).fastJsonJsonView();
        fastJsonProperties.setSerializerFeatures(new SerializerFeature[]{});
        new FastJsonAutoConfiguration(this.fastJsonProperties).fastJsonJsonView();
        fastJsonProperties.setSerializerFeatures(new SerializerFeature[]{SerializerFeature.BrowserSecure});
        new FastJsonAutoConfiguration(this.fastJsonProperties).fastJsonJsonView();
    }
}
