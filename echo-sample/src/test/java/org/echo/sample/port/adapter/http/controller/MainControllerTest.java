package org.echo.sample.port.adapter.http.controller;

import org.echo.share.config.SpringMvcConfiguration;
import org.echo.test.web.AbstractSpringControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

@ContextHierarchy(@ContextConfiguration(
        classes = {
                SpringMvcConfiguration.class,
                MainController.class
        }))
@DisplayName("MainController Test")
public class MainControllerTest extends AbstractSpringControllerTest {

    @InjectMocks
    @Autowired
    MainController controller;

    @Test
    void onIndex() throws Exception{
        this.mvc.perform(post("/index").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
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

        this.mvc.perform(get("/index")
                .param("local", "" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(view().name("/index"))
                .andExpect(content().string(startsWith("<!DOCTYPE html>")))
                .andExpect(content().string(containsString("</html>")))
                .andExpect(content().string(endsWith("</html>")));
    }
}