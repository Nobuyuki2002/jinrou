package oit.is.ouchi.jinrou.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.ouchi.jinrou.model.Roles;
import oit.is.ouchi.jinrou.model.RolesMapper;
import oit.is.ouchi.jinrou.model.Rooms;
import oit.is.ouchi.jinrou.model.RoomsMapper;
import oit.is.ouchi.jinrou.model.Users;
import oit.is.ouchi.jinrou.model.UsersMapper;
import oit.is.ouchi.jinrou.service.AsyncEntry;

import oit.is.ouchi.jinrou.model.Count;

@Controller
@RequestMapping("/jinrou")
public class JinrouController {

  @Autowired
  UsersMapper usersMapper;

  @Autowired
  RoomsMapper roomsMapper;

  @Autowired
  AsyncEntry entryService;

  @Autowired
  RolesMapper rolesMapper;

  @GetMapping("/")
  public String jinrou(ModelMap model, Principal prin) {
    Users tmp = null;
    if ((tmp = usersMapper.selectByName(prin.getName())) == null) {
      return "createUser.html";
    }
    model.addAttribute("roomId", tmp.getId());
    model.addAttribute("userName", tmp.getPname());
    return "gameWait.html";
  }

  @PostMapping("entry")
  @Transactional
  public String entry(@RequestParam String pname, ModelMap model, Principal prin) {
    Users user = new Users(prin.getName(), pname, 1, 3, true, -1);
    Users tmp = new Users();
    if ((tmp = usersMapper.selectByName(pname)) == null) {
      usersMapper.insertUsers(user);
      model.addAttribute("roomId", user.getId());
      model.addAttribute("userName", user.getPname());
    } else {
      model.addAttribute("roomId", tmp.getId());
      model.addAttribute("userName", tmp.getPname());
    }
    return "gameWait.html";
  }

  @GetMapping("match")
  public String match(@RequestParam Integer roomId, Principal prin, ModelMap model) {
    Users user;
    Rooms room = roomsMapper.selectById(roomId);
    Roles roles;
    Count count = usersMapper.selectCountByRoomId(roomId);
    int wolfNum = room.getWolfNum();
    if (wolfNum == 0) {
      wolfNum = (int) (Math.random() * count.getCount()) + 1;
      room.setWolfNum(wolfNum);
      roomsMapper.updateWolfNum(room);
    }
    System.out.println(wolfNum);
    ArrayList<Users> roomMember = usersMapper.selectByRoomId(roomId);
    for (Users tmp : roomMember) {
      if (wolfNum == tmp.getId()) {
        tmp.setRoles(2);
      } else {
        tmp.setRoles(1);
      }
      usersMapper.updateRole(tmp);
    }
    user = usersMapper.selectByName(prin.getName());
    roles = rolesMapper.selectRoles(user.getRoles());
    model.addAttribute("roomName", room.getRoomName());
    model.addAttribute("roles", roles.getRolName());
    return "match.html";
  }

  @GetMapping("/wait-start")
  public SseEmitter result() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.entryService.syncCheckEntry(sseEmitter);
    return sseEmitter;
  }
}
