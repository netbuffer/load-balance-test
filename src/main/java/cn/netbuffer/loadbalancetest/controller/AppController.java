package cn.netbuffer.loadbalancetest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping(value = "/")
public class AppController {

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private Environment environment;

    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model) {
        log.info("get method");
        model.addAttribute("env", environment);
        return "app";
    }

    @RequestMapping(value = "putsession", method = RequestMethod.GET)
    @ResponseBody
    public Object putSession(HttpSession session) {
        if (session.getAttribute("user") == null) {
            session.setAttribute("user", this + environment.getProperty("server.port"));
        }
        return session;
    }
}