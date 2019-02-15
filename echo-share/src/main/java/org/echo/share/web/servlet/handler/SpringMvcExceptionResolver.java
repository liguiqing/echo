package org.echo.share.web.servlet.handler;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.share.web.servlet.http.ModelAndViewer;
import org.echo.share.web.servlet.http.ResponseText;
import org.echo.share.web.servlet.http.ResponseTextFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@AllArgsConstructor
public class SpringMvcExceptionResolver extends SimpleMappingExceptionResolver {

    private ResponseTextFactory responseTextFactory;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                              Exception ex) {
        log.error("系统异常 ：{}", ThrowableToString.toString(ex));
        String viewName = determineViewName(ex,request);

        Integer statusCode = determineStatusCode(request, viewName);
        if (statusCode != null) {
            applyStatusCodeIfPossible(request, response, statusCode);
        }
        return createModelAndView(viewName,request, ex);
    }

    @Override
    protected String determineViewName(Exception ex,HttpServletRequest request){
        log.debug("Get View Name");
        return super.determineViewName(ex, request);
    }

    private ModelAndView createModelAndView(String viewName, HttpServletRequest request, Exception ex) {
        String code = ex.getMessage();
        ResponseText responseText = responseTextFactory.lookup(request.getParameter("local"));
        return new ModelAndViewer(viewName,responseText).code(code).failure().create();
    }
}