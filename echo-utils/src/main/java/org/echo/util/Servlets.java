package org.echo.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class Servlets {

    private Servlets(){
        throw new AssertionError("No org.echo.util.Servlets instances for you!");
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static Map<String,String> getParameterMap(HttpServletRequest request){
        Map<String,String[]> ps = request.getParameterMap();
        HashMap<String,String> pss = new HashMap<>();
        Iterator<String> keys = ps.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            String[] values = ps.get(key);
            if(values == null || values.length == 0){
                pss.put(key,"");
            }else{
                pss.put(key,values[0]);
            }
        }
        return pss;
    }
}