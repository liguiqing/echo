package org.echo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class Springs implements ApplicationContextAware {
    private Springs() {
        throw new AssertionError("No org.echo.util.Springs instances for you!");
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Springs.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name) {
        return (T) Springs.applicationContext.getBean(name);
    }

    public static <T> T getBean(Class clazz) {
        return (T) Springs.applicationContext.getBean(clazz);
    }

    public static void registerBean(String beanId, String beanClassName, Map<String,Object> pvs) {
        BeanDefinition bdef = new GenericBeanDefinition();
        bdef.setBeanClassName(beanClassName);
        if(pvs != null) {
            for (String p : pvs.keySet()) {
                bdef.getPropertyValues().add(p, pvs.get(p));
            }
        }

        DefaultListableBeanFactory fty = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        fty.registerBeanDefinition(beanId, bdef);
    }

    public static void removeBean(String beanName){
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
        registry.removeBeanDefinition(beanName);
    }
}