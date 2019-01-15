package org.echo.test.web;

import org.echo.TestBean;
import org.echo.test.SampleTestServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Liguiqing
 * @since V1.0
 */

@DisplayName("SampController Test")

class SampleControllerTest extends AbstractSpringControllerTest {

    @Mock
    private SampleTestServiceInterface<TestBean,String> serviceInterface;

    @BeforeEach
    public void before(){
        super.before();
        doNothing().when(serviceInterface).doSomething(any(TestBean.class));
        Collection<TestBean> testBeans = new ArrayList<>();
        testBeans.add(new TestBean().setMaster("master"));
        testBeans.add(new TestBean().setMaster("master1"));
        when(serviceInterface.findSometingAll()).thenReturn(testBeans);
        when(serviceInterface.getSomething(any(String.class))).thenReturn(new TestBean().setMaster("master"));
        SampleController controller = new SampleController(serviceInterface);
        injectNoneFieldsInConstructor(controller, Arrays.asList(new FieldMapping("uuid", "uuid")));
        applyController(controller);
    }

    @Test
    void onPost()throws Exception{
        TestBean tb = new TestBean().setMaster("master");
        String content = toJsonString(tb);
        this.mvc.perform(post("/test").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(content))
                .andExpect(jsonPath("$.bean.master", is(tb.getMaster())))
                .andExpect(jsonPath("$.uuid", is("uuid")))
                .andExpect(view().name("testPost"));
    }

    @Test
    void onGet()throws Exception{
        this.mvc.perform(get("/test/master").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).param("excludes","master"))
                .andExpect(jsonPath("$.bean.master", is("master")))
                .andExpect(jsonPath("$.result[0].master", is("master")))
                .andExpect(view().name("testGet"));
    }

    @Test
    void onPut()throws Exception{
        TestBean tb = new TestBean().setMaster("master");
        String content = toJsonString(tb);
        this.mvc.perform(put("/test").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(content))
                .andExpect(jsonPath("$.bean.master", is(tb.getMaster())))
                .andExpect(jsonPath("$.uuid", is("uuid")))
                .andExpect(view().name("testPost"));
    }

    @Test
    void onDelete()throws Exception{
        TestBean tb = new TestBean().setMaster("master");
        String content = toJsonString(tb);
        this.mvc.perform(delete("/test").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(content))
                .andExpect(jsonPath("$.bean.master", is(tb.getMaster())))
                .andExpect(jsonPath("$.uuid", is("uuid")))
                .andExpect(view().name("testPost"));
    }


    @Test
    public void testThrows(){
        assertThrows(ControllerTestException.class,()->toJsonString(new Object()));
        assertThrows(ControllerTestException.class,()->writField(new ArrayList(), new FieldMapping("uuid", "uuid")));
        //writField(new ArrayList(), new FieldMapping("uuid", "uuid"));
    }

}