package org.echo.test.web;

import lombok.extern.slf4j.Slf4j;
import org.echo.TestBean;
import org.echo.test.SampleTestServiceInterface;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liguiqing
 * @since V3.0
 */
@Slf4j
@Controller
public class SampleController {

    private String uuid = "uuid";

    private SampleTestServiceInterface<TestBean,String> serviceInterface;

    public SampleController(SampleTestServiceInterface serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public ModelAndView onPost(@RequestBody TestBean bean){
        log.debug("URL /test Method POST");
        return getModelAndView(bean);
    }

    @RequestMapping(value = "/test/{id}",method = RequestMethod.GET)
    public ModelAndView onGet(@PathVariable  String id, @RequestParam(required = false) String excludes){
        log.debug("URL /test/{} Method GET",id);
        TestBean bean = serviceInterface.getSomething(id);
        Collection<TestBean> allBeans = serviceInterface.findSometingAll();
        List<TestBean> result =  allBeans.stream().filter(b -> b.getMaster().equals(excludes)).collect(Collectors.toList());
        HashMap mod = new HashMap<>();
        mod.put("bean", bean);
        mod.put("result", result);
        return new ModelAndView("testGet", mod);
    }

    @RequestMapping(value = "/test",method = RequestMethod.PUT)
    public ModelAndView onUpdate(@RequestBody TestBean bean){
        log.debug("URL /test Method PUT");
        return getModelAndView(bean);
    }

    private ModelAndView getModelAndView(TestBean bean) {
        serviceInterface.doSomething(bean);
        HashMap mod = new HashMap<>();
        mod.put("bean", bean);
        mod.put("uuid", uuid);
        return new ModelAndView("testPost", mod);
    }

    @RequestMapping(value = "/test",method = RequestMethod.DELETE)
    public ModelAndView onDelete(@RequestBody TestBean bean){
        log.debug("URL /test Method DELETE");
        return getModelAndView(bean);
    }

}