package com.tinthon.ncoa.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class HomeController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private Environment env;
    @GetMapping("/")
    public String home() {
        httpSession.setAttribute("name", "yang");
        return "hello,world!" + httpSession.getId() + ", " + httpSession.getAttribute("name");
    }
}
