package org.echo.share.web.servlet.http;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.util.Servlets;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Liguiqing
 * @since V1.0
 */

@Slf4j
public abstract class AbstractHttpController {

    @Autowired
    protected ResponseTextFactory responseTextFactory;

    protected void output(String content){
        this.output(content,Servlets.getResponse());
    }

    protected void output(String content, HttpServletResponse response){
        try {
            PrintWriter out = response.getWriter();
            out.print(content);
        } catch (IOException e) {
            log.error(ThrowableToString.toString(e));
        }
    }

    protected ModelAndViewer modelAndViewer(String viewName){
        return modelAndViewer(viewName,"000000");
    }

    protected ModelAndViewer modelAndViewer(String viewName,String code){
        String local = Servlets.getRequest().getParameter("local");
        return new ModelAndViewer(viewName,responseTextFactory.lookup(local));
    }
}