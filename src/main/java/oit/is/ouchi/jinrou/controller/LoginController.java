package oit.is.ouchi.jinrou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.ouchi.jinrou.model.LoginUserMapper;
import oit.is.ouchi.jinrou.model.LoginUser;

@Controller
public class LoginController {

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  LoginUserMapper loginUserMapper;

  @GetMapping("/registMenu")
  public String registMenu() {
    return "registUser.html";
  }

  @PostMapping("/regist")
  @Transactional
  public String registUser(@RequestParam String userName, @RequestParam String userPasswd, ModelMap model) {

    if (userName.equals("") || userPasswd.equals("")) {
      model.addAttribute("error1", "ユーザ名とパスワードを入力してください");
      return "registUser.html";
    }

    // パスワードをhash化
    userPasswd = passwordEncoder.encode(userPasswd);

    if ((loginUserMapper.selectLoginUserByName(userName)) != null) {
      model.addAttribute("error2", "そのユーザ名は使用できません");
      return "registUser.html";
    }

    LoginUser loginUser = new LoginUser(userName, userPasswd, "ROLE_USER");
    loginUserMapper.insertLoginUser(loginUser);
    model.addAttribute("message", "登録完了:topページへ戻る");
    return "registUser.html";
  }
}
