package org.echo.test.web;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Spring Controller Test 超类
 *
 *
 * @author Liguiqing
 * @since V1.0
 */
public abstract class AbstractSpringControllerTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected MockMvc mvc;

    protected List<ViewResolver> viewResolvers  = Lists.newArrayList();

    @BeforeAll
    public void before(){
        MockitoAnnotations.initMocks(this);
    }

    protected void injectNoneFieldsInConstructor(Object object, Collection<FieldMapping> fieldMappings){
        fieldMappings.forEach(m->writField(object, m));
    }

    protected void writField(Object o,FieldMapping m){
        try {
            FieldUtils.writeField(o,m.getField(),m.getObject(),true);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new ControllerTestException(e);
        }
    }

    protected void applyController(Object... controller){
        StandaloneMockMvcBuilder mvcBuilder =  MockMvcBuilders.standaloneSetup(controller);
        ContentNegotiationManagerFactoryBean contentNegotiationManager = new ContentNegotiationManagerFactoryBean();
        contentNegotiationManager.setDefaultContentType(MediaType.APPLICATION_JSON);
        contentNegotiationManager.addMediaType("html", MediaType.TEXT_HTML);
        contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);
        contentNegotiationManager.build();
        ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
        viewResolver.setContentNegotiationManager(contentNegotiationManager.getObject());
        viewResolver.setDefaultViews(Arrays.asList(new MappingJackson2JsonView()));
        viewResolver.setViewResolvers(viewResolvers);
        mvcBuilder.setViewResolvers(viewResolver);
        mvcBuilder.setContentNegotiationManager(contentNegotiationManager.getObject());
        mvcBuilder.setMessageConverters(fastJsonMessageConverter(), new StringHttpMessageConverter(),new FormHttpMessageConverter());
        this.mvc = mvcBuilder.build();
    }

    protected FastJsonHttpMessageConverter fastJsonMessageConverter(){
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
        return c1;
    }

    protected String toJsonString(Object o){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new ControllerTestException(e);
        }
    }
}