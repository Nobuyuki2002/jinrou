package oit.is.ouchi.jinrou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.ouchi.jinrou.model.Users;
import oit.is.ouchi.jinrou.model.UsersMapper;
import oit.is.ouchi.jinrou.security.JinrouAuthConfiguration;

@Controller
public class CreateUserController {

  @Autowired
  UsersMapper usersMapper;

  @GetMapping("/new")
  public String createNewUser() {
    return "createNewUser.html";
  }

  @PostMapping("/new")
  public String createNewUser(@RequestParam String pname, @RequestParam String passwd, ModelMap model) {
    JinrouAuthConfiguration sec = new JinrouAuthConfiguration();
    String message = "登録が完了しました";
    Users user = new Users(pname, sec.passwordEncoder().encode(passwd), 1, 1, true, -1);
    usersMapper.insertUsers(user);
    model.addAttribute("massag", message);
    return "createNewUser.html";
  }
}
