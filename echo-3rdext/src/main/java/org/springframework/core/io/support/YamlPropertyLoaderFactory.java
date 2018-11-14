package org.springframework.core.io.support;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;

/**
 * yml文件加载器
 * @author Liguiqing
 * @since V1.0
 */

public class YamlPropertyLoaderFactory extends DefaultPropertySourceFactory{
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (resource == null){
            return super.createPropertySource(name, resource);
        }

        return new YamlPropertySourceLoader().load(name, new InputStreamResource(resource.getInputStream())).get(0);
    }

}