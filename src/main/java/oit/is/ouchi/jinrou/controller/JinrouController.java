package oit.is.ouchi.jinrou.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Select;
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
import oit.is.ouchi.jinrou.service.AsyncGameThread;
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

  @Autowired
  AsyncGameThread gameService;

  int killMaxIndex = -1;
  Boolean killFlag = true;

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
    Users user = new Users(prin.getName(), pname, 1, 3);
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
    ArrayList<Users> users;
    Rooms room = roomsMapper.selectById(roomId);
    Roles roles;
    Count count = usersMapper.selectCountByRoomId(roomId);
    int wolfNum = room.getWolfNum();
    user = usersMapper.selectByName(prin.getName());
    if (user.isDeath()) {
      return "death.html";
    }
    if (room.getRoopCount() < 0) {
      room.setRoopCount(((-1) * room.getRoopCount()) + 1);
      roomsMapper.updateRoopCount(room);
    }
    if (wolfNum == 0) {
      wolfNum = (int) (Math.random() * count.getCount()) + 1;
      room.setWolfNum(wolfNum);
      roomsMapper.updateWolfNum(room);
      ArrayList<Users> roomMember = usersMapper.selectByRoomId(roomId);
      for (Users tmp : roomMember) {
        if (wolfNum == tmp.getId()) {
          tmp.setRoles(2);
        } else {
          tmp.setRoles(1);
        }
        usersMapper.updateRole(tmp);
      }
    }
    roles = rolesMapper.selectRoles(user.getRoles());
    model.addAttribute("roomName", room.getRoomName());
    model.addAttribute("roles", roles);
    users = usersMapper.selectAliveUsers(roomId);
    model.addAttribute("users", users);
    model.addAttribute("roopCount", room.getRoopCount());
    return "match.html";
  }

  @GetMapping("job")
  public String job(@RequestParam Integer id, Principal prin, ModelMap model) {
    Users loginUser = usersMapper.selectByName(prin.getName());
    loginUser.setJobVote(id);
    usersMapper.updateJobVote(loginUser);
    loginUser.setKillVote(-1);
    usersMapper.updateKillVote(loginUser);
    model.addAttribute("roomId", loginUser.getRoom());
    return "disc.html";
  }

  @GetMapping("vote")
  public String vote(@RequestParam Integer id, Principal prin, ModelMap model) {
    Users loginUser = usersMapper.selectByName(prin.getName());
    loginUser.setKillVote(id);
    usersMapper.updateKillVote(loginUser);
    model.addAttribute("roomId", loginUser.getRoom());
    Rooms room = roomsMapper.selectById(loginUser.getRoom());
    killMaxIndex = -1;
    if (room.getRoopCount() >= 0) {
      room.setRoopCount(room.getRoopCount() * -1);
    }
    return "discFinish.html";
  }

  @GetMapping("result")
  public String result(@RequestParam Integer roomId, Principal prin, ModelMap model) {
    Users loginUser = usersMapper.selectByName(prin.getName());
    loginUser.setJobVote(-1);
    usersMapper.updateJobVote(loginUser);

    // 死ぬ人が決まっていた場合に処理
    if (killMaxIndex < 0) {
      ArrayList<Users> users = usersMapper.selectAliveUsers(roomId);
      int[] vote = new int[users.size()];

      for (Users user : users) {
        vote[user.getKillVote() - 1]++;
      }
      int max = vote[0];
      killMaxIndex = 0;
      killFlag = true;
      for (int i = 1; i < vote.length; i++) {
        if (max < vote[i]) {
          killMaxIndex = i;
          max = vote[i];
        } else if (max == vote[i]) {
          killFlag = false;
          break;
        }
      }
      // 人狼の数と村人の数を比べてゲームの終了を判定する
    }

    if (killFlag) {
      Users deathUser = usersMapper.selectById(killMaxIndex + 1);
      model.addAttribute("death", deathUser);
      deathUser.setDeath(true);
      usersMapper.updateDeath(deathUser);
    }
    // 死ぬ人を決める処理
    model.addAttribute("roomId", roomId);
    return "result.html";
  }

  @GetMapping("/wait-start")
  public SseEmitter result() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.entryService.syncCheckEntry(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("/wait-game")
  public SseEmitter jobVote() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.gameService.syncCheckJobVote(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("/wait-vote")
  public SseEmitter killVote() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.gameService.syncCheckKillVote(sseEmitter);
    return sseEmitter;
  }
}
