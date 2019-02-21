package org.echo.share.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.share.web.servlet.handler.SpringMvcExceptionResolver;
import org.echo.share.web.servlet.http.ResponseTextFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@Configuration
public class SpringMvcConfiguration extends WebMvcConfigurationSupport {

    @Value("${app.commons.charset:UTF-8}")
    private String charset;

    @Value("${app.commons.dateFormat:yyyy-MM-dd HH:mm:sss}")
    private String dateFormat ;

    @Value("${spring.freemarker.cache:false}")
    private boolean freeMarkerViewResolverCached;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
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
        c1.setFastJsonConfig(newFastJsonConfig());
        converters.add(c1);
    }

    @Bean
    public FastJsonConfig fastJsonConfig(){
        return newFastJsonConfig();
    }

    private FastJsonConfig newFastJsonConfig(){
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(Charset.forName(charset));
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteEnumUsingToString);
        fastJsonConfig.setDateFormat(dateFormat);
        return fastJsonConfig;
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
    public FastJsonJsonView fastJsonJsonView(FastJsonConfig fastJsonConfig) {
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


    @Bean
    public ViewResolver viewResolverFtl(Optional<FreemarkerStaticModels> freemarkerStaticModels) {
        return freemarkerViewResolver(".ftl",0,freemarkerStaticModels);
    }

    @Bean
    public ViewResolver viewResolverHtml(Optional<FreemarkerStaticModels> freemarkerStaticModels) {
        return freemarkerViewResolver(".html",1,freemarkerStaticModels);
    }

    private ViewResolver freemarkerViewResolver(String suffix,int order,
                                                Optional<FreemarkerStaticModels> freemarkerStaticModels){
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(freeMarkerViewResolverCached);
        resolver.setViewClass(org.springframework.web.servlet.view.freemarker.FreeMarkerView.class);
        resolver.setRequestContextAttribute("request");
        resolver.setExposeRequestAttributes(true);
        resolver.setExposeSessionAttributes(true);
        resolver.setOrder(order);
        resolver.setSuffix(suffix);
        resolver.setContentType("text/html;charset=UTF-8");
        freemarkerStaticModels.ifPresent(mf->resolver.setAttributesMap((Map)mf));
        return resolver;
    }


    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
        factory.setTemplateLoaderPath("classpath:META-INF/ftl");
        factory.setDefaultEncoding("UTF-8");
        factory.setPreferFileSystemAccess(false);
        FreeMarkerConfigurer result = new FreeMarkerConfigurer();
        try {
            freemarker.template.Configuration configuration = factory.createConfiguration();
            configuration.setClassicCompatible(true);
            result.setConfiguration(configuration);
        } catch (IOException | TemplateException e) {
            log.error(ThrowableToString.toString(e));
        }

        Properties settings = new Properties();
        settings.put("template_update_delay", "0");
        settings.put("default_encoding", "UTF-8");
        settings.put("number_format", "0.######");
        settings.put("classic_compatible", true);
        settings.put("template_exception_handler", "ignore");
        result.setFreemarkerSettings(settings);
        return result;
    }
}