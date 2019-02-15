package org.echo.share.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.echo.share.web.servlet.handler.SpringMvcExceptionResolver;
import org.echo.share.web.servlet.http.ResponseTextFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Liguiqing
 * @since V1.0
 */

@Configuration
public class SpringMvcConfiguration extends WebMvcConfigurationSupport {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        super.configureMessageConverters(converters);
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteEnumUsingToString);
        FastJsonHttpMessageConverter c1 = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_STREAM_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        c1.setSupportedMediaTypes(supportedMediaTypes);
        c1.setFastJsonConfig(fastJsonConfig);
        converters.add(c1);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/static/resources/");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

    @Bean
    public FastJsonJsonView fastJsonJsonView(@Value("${app.commons.dateFormat:yyyy-MM-dd HH:mm:sss}") String dateFormat) {
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteEnumUsingToString);
        fastJsonConfig.setDateFormat(dateFormat);
        FastJsonJsonView view = new FastJsonJsonView();
        view.setFastJsonConfig(fastJsonConfig);
        return view;
    }


    @Bean(name = "viewResolver")
    @Primary
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver(FastJsonJsonView fastJsonJsonView) {
        List<View> views = new ArrayList<>();
        views.add(fastJsonJsonView);
        ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
        viewResolver.setDefaultViews(views);
        return viewResolver;
    }

    @Bean
    public ResponseTextFactory responseTextFactory(){
        return new ResponseTextFactory() {};
    }

    @Bean("exceptionResolver")
    public SpringMvcExceptionResolver exceptionResolver(ResponseTextFactory responseTextFactory){
        SpringMvcExceptionResolver exceptionResolver = new SpringMvcExceptionResolver(responseTextFactory);
        exceptionResolver.setDefaultErrorView("/404");
        Properties mappings = new Properties();
        mappings.setProperty("java.lang.Exception", "505");
        exceptionResolver.setExceptionMappings(mappings);
        Properties statusCodes = new Properties();
        statusCodes.setProperty("/500","500");
        statusCodes.setProperty("/401","401");
        statusCodes.setProperty("/403","403");
        statusCodes.setProperty("/404","404");
        statusCodes.setProperty("/405","405");
        exceptionResolver.setStatusCodes(statusCodes);
        return  exceptionResolver;
    }
}