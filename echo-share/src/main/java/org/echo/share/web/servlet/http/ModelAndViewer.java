package org.echo.share.web.servlet.http;

import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class ModelAndViewer {

    private HashMap<String, Object> model;

    private String viewName;

    private String code;

    private boolean success = true;

    private ResponseText responseText;

    public ModelAndViewer(String viewName, ResponseText responseText) {
        this.viewName = viewName;
        this.responseText = responseText;
        this.model = new HashMap<>();
    }

    public ModelAndViewer data(String name,Object data){
        this.model.put(name,data);
        return this;
    }

    public ModelAndViewer code(String code){
        this.code = code;
        return this;
    }

    public ModelAndViewer failure(){
        this.success = false;
        return this;
    }

    public ModelAndView create(){
        Responser response = new Responser(this.success,this.code,this.responseText.getText(this.code));
        this.data("status", response);
        return new ModelAndView(this.viewName, this.model);
    }
}