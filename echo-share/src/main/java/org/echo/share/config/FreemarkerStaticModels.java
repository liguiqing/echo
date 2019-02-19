package org.echo.share.config;


import com.google.common.collect.Maps;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.echo.exception.ThrowableToString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class FreemarkerStaticModels extends HashMap<Object, Object> {

    public FreemarkerStaticModels(){
        this(defaultStaticModels());
    }

    public FreemarkerStaticModels(PropertiesConfiguration propertiesConfig) {
        Iterator it = propertiesConfig.getKeys();
        while (it.hasNext()) {
            Object key = it.next();
            put(key, getModel(propertiesConfig.getString(String.valueOf(key))));
        }
    }

    public FreemarkerStaticModels(Map<String, String> classMap) {
        classMap.keySet().forEach(key -> put(key, getModel(classMap.get(key))));
    }

    public static Map<String, String> defaultStaticModels(){
        Map<String,String> model = Maps.newHashMap();
        model.put("StringUtils","org.apache.commons.lang3.StringUtils");
        model.put("Shiros","org.echo.shiro.Shiros");
        return model;
    }

    private TemplateHashModel getModel(String packageName) {
        BeansWrapper wrapper = new BeansWrapperBuilder(new Version("2.3.28")).build();
        TemplateHashModel staticModels = wrapper.getStaticModels();
        try {
            return (TemplateHashModel) staticModels.get(packageName);
        } catch (TemplateModelException e) {
            log.error(ThrowableToString.toString(e));
        }
        return null;
    }

}