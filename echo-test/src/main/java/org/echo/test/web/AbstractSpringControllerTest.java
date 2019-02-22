package org.echo.test.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Spring Controller Test 超类
 *
 *
 * @author Liguiqing
 * @since V1.0
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@EnableWebMvc
public abstract class AbstractSpringControllerTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mvc;

    @BeforeEach
    public void before(){
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
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