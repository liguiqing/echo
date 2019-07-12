package org.echo.shiro.authc.filter;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 *  扩展formfilter 以支持ajax登录
 * </pre>
 *
 * @author liguiqing
 * @since V1.0.0 2019-07-12 19:39
 **/
@Slf4j
public class WebLoginFilter extends FormAuthenticationFilter {

    private String ajaxLoginFailureUrl;

    public WebLoginFilter(String ajaxLoginFailureUrl) {
        this.ajaxLoginFailureUrl = ajaxLoginFailureUrl;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (isAjaxRequest(httpServletRequest)) {
            httpServletResponse.sendRedirect(ajaxLoginFailureUrl);
        }else{
            super.onAccessDenied(request, response);
        }
        return false;
    }

    private boolean isAjaxRequest(HttpServletRequest request) {

        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
            return true;
        }

        return false;
    }
}
