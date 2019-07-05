package org.echo.springboot.starter.fastjson;


import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <pre>
 *  fastjson 配置
 * </pre>
 *
 * @author liguiqing
 * @since V1.0.0 2019-07-05 15:02
 **/
@Data
@ConfigurationProperties(prefix = "spring.fastjson")
public class FastJsonProperties {
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";

    private SerializerFeature[] serializerFeatures;
}
