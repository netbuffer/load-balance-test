package cn.netbuffer.loadbalancetest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping
@Slf4j
public class AppController {

    @Resource
    private ApplicationContext applicationContext;

    @RequestMapping(method = RequestMethod.GET)
    public String get(Model model) {
        log.info("get method");
        model.addAttribute("env", applicationContext.getEnvironment());
        return "app";
    }
}
