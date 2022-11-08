package oit.is.ouchi.jinrou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JinrouController {

  @GetMapping("/jinrou")
  public String jinrou() {
    return "jinrou.html";
  }

}
