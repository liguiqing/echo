package org.echo.sample.port.adapter.http.controller;

import org.echo.test.web.AbstractSpringControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@ContextHierarchy({
        @ContextConfiguration(classes= {MainController.class})
})
@DisplayName("MainController Test")
public class MainControllerTest extends AbstractSpringControllerTest {

    @BeforeEach
    public void before(){
        super.before();
        MainController controller = new MainController();
        applyController(controller);
    }

    @Test
    void onIndex() throws Exception{

        this.mvc.perform(post("/index").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.success", is(true)))
                .andExpect(view().name("/index"));

        this.mvc.perform(get("/index").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.success", is(true)))
                .andExpect(view().name("/index"));

        this.mvc.perform(get("/home").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.success", is(true)))
                .andExpect(view().name("/index"));
        this.mvc.perform(get("/").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.success", is(true)))
                .andExpect(view().name("/index"));

        this.mvc.perform(get("").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.success", is(true)))
                .andExpect(view().name("/index"));

    }
}