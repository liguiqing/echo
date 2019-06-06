package org.echo.test.web;

import org.assertj.core.util.Lists;
import org.echo.TestBean;
import org.echo.test.SampleTestServiceInterface;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Liguiqing
 * @since V1.0
 */
@ContextHierarchy(@ContextConfiguration(
        classes = {
                SampleController.class
        }))
@DisplayName("SampleController Test")
class SampleControllerTest extends AbstractSpringControllerTest{
    @Mock
    private SampleTestServiceInterface<TestBean,String> serviceInterface;

    @InjectMocks
    @Autowired
    SampleController controller;

    @Test
    void onPost()throws Exception{
        TestBean tb = new TestBean().setMaster("master");
        String content = toJsonString(tb);
        this.mvc.perform(post("/test").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(content)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("testPost"));
    }

    @Test
    void onGet()throws Exception{
        TestBean tb = new TestBean().setMaster("master");
        TestBean tb2 = new TestBean().setMaster("master1");
        when(serviceInterface.getSomething(any(String.class))).thenReturn(tb);
        List<TestBean> beans = Lists.newArrayList();
        beans.add(tb);
        beans.add(tb2);
        when(serviceInterface.findSometingAll()).thenReturn(beans);
        this.mvc.perform(get("/test/master").contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .param("excludes","master")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("testGet"));
    }

    @Test
    void onPut()throws Exception{
        TestBean tb = new TestBean().setMaster("master");
        String content = toJsonString(tb);
        this.mvc.perform(put("/test").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(content)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("testPost"));
    }

    @Test
    void onDelete()throws Exception{
        TestBean tb = new TestBean().setMaster("master");
        String content = toJsonString(tb);
        this.mvc.perform(delete("/test").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(content)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("testPost"));
    }

    @Test
    public void testThrows(){
        assertThrows(ControllerTestException.class,()->toJsonString(new Object()));
    }

}