package com.hedvig.backoffice.web;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

  @RequestMapping(
      value = {
        "/",
        "/login/**",
        "/assets",
        "/members/**",
        "/member_insurance/**",
        "/dashboard",
        "/claims/**",
        "/questions/**",
        "/payments"
      })
  @ResponseBody
  public Resource index() {
    return new ClassPathResource("static/index.html");
  }
}
