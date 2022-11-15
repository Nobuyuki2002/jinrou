package oit.is.ouchi.jinrou.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.ouchi.jinrou.model.Users;
import oit.is.ouchi.jinrou.model.UsersMapper;

@Controller
@RequestMapping("/jinrou")
public class JinrouController {

  @Autowired
  UsersMapper usersMapper;

  @GetMapping("/")
  public String jinrou() {
    return "jinrou.html";
  }

  @PostMapping("entry")
  @Transactional
  public String entry(@RequestParam String pname) {
    Users user = new Users(1, pname, 1, true, -1);
    if (usersMapper.selectByName(pname) == null) {
      usersMapper.insertUsers(user);
    }
    return "jinrou.html";
  }

}
