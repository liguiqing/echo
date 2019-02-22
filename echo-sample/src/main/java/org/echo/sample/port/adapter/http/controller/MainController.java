package org.echo.sample.port.adapter.http.controller;

import lombok.extern.slf4j.Slf4j;
import org.echo.share.web.servlet.http.AbstractHttpController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@Controller
public class MainController extends AbstractHttpController {

    @RequestMapping(value={"", "/", "index","home"})
    public ModelAndView onIndex(HttpServletRequest request){
        log.debug("URL {}",request.getRequestURI());
        return modelAndViewer("/index").create();
    }
}