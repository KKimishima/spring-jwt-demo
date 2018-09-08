package com.github.kkimishima.springjwtdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
  @RequestMapping(method = RequestMethod.GET)
  public String index() {
    return "Hello";
  }
}
