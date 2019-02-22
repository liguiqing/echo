package org.echo.test.web;

import org.echo.TestBean;
import org.echo.test.SampleTestServiceInterface;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
                .accept(MediaType.APPLICATION_JSON).content(content)).andDo(print());
//                .andDo(print())
//                .andExpect(jsonPath("$.bean.master", is(tb.getMaster())))
//                .andExpect(jsonPath("$.uuid", is("uuid")))
//                .andExpect(view().name("testPost"));
    }

    @Test
    void onGet()throws Exception{
        this.mvc.perform(get("/test/master").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).param("excludes","master")).andDo(print());
//                .andExpect(jsonPath("$.bean.master", is("master")))
//                .andExpect(jsonPath("$.result[0].master", is("master")))
//                .andExpect(view().name("testGet"));
    }

    @Test
    void onPut()throws Exception{
        TestBean tb = new TestBean().setMaster("master");
        String content = toJsonString(tb);
        this.mvc.perform(put("/test").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(content)).andDo(print());
//                .andExpect(jsonPath("$.bean.master", is(tb.getMaster())))
//                .andExpect(jsonPath("$.uuid", is("uuid")))
//                .andExpect(view().name("testPost"));
    }

    @Test
    void onDelete()throws Exception{
        TestBean tb = new TestBean().setMaster("master");
        String content = toJsonString(tb);
        this.mvc.perform(delete("/test").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(content)).andDo(print());
//                .andExpect(jsonPath("$.bean.master", is(tb.getMaster())))
//                .andExpect(jsonPath("$.uuid", is("uuid")))
//                .andExpect(view().name("testPost"));
    }

    @Test
    public void testThrows(){
        assertThrows(ControllerTestException.class,()->toJsonString(new Object()));
    }

}