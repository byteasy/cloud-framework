package com.byteasy.platform.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2Controller {

    @GetMapping("/login")
    public String login() {
        return "login"; // 指向 templates/login.html
    }

}

