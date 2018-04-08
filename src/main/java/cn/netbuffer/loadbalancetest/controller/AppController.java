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
import javax.servlet.http.HttpServletRequest;
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
    public String get(ModelMap model, HttpSession session, HttpServletRequest request) {
        log.info("get method");
        if (session.getAttribute("user") == null) {
            session.setAttribute("user", this + "-" + environment.getProperty("server.port"));
        }
        model.addAttribute("ip", getRemortIP(request));
        model.addAttribute("env", environment);
        return "app";
    }

    public static String getRemortIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));

        }
        return ip;
    }

    @RequestMapping(value = "putsession", method = RequestMethod.POST)
    @ResponseBody
    public Object putSession(HttpSession session, String key, String val) {
        session.setAttribute(key, val);
        return session.getAttribute(key);
    }
}