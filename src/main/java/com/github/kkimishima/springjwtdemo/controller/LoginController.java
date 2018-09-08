package com.github.kkimishima.springjwtdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
  @RequestMapping(method = RequestMethod.GET, value = "/loginForm")
  public String loginForm() {
    return "loginForm";
  }
}
