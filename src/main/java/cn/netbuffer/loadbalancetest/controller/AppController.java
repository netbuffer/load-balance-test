package cn.netbuffer.loadbalancetest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Controller
@RequestMapping(value = "/")
public class AppController {

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private Environment environment;
    private AtomicLong requestCount = new AtomicLong();

    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model, HttpSession session, HttpServletRequest request) {
        log.info("get method");
        if (session.getAttribute("user") == null) {
            session.setAttribute("user", this + "-" + environment.getProperty("server.port"));
            session.setAttribute("jdk", System.getProperty("java.version"));
            session.setAttribute("computer", System.getenv("COMPUTERNAME") + "-" + System.getenv("USERNAME"));
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

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public long getRequest(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "pwd", required = false) String pwd) {
        if (!StringUtils.isEmpty(name)) {
            log.info("name:{},pwd:{}", name, pwd);
        }
        return requestCount.getAndIncrement();
    }

    @RequestMapping(value = "requestcount", method = RequestMethod.GET)
    @ResponseBody
    public long getRequestCount() {
        return requestCount.get();
    }

    @RequestMapping(value = "getLocalHost", method = RequestMethod.GET)
    @ResponseBody
    public Map getLocalHost() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        Map data = new HashMap(2);
        data.put("hostName", address.getHostName());
        data.put("address", address.getHostAddress());
        return data;
    }
}