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
  @Transactional
  public String match(@RequestParam Integer roomId, Principal prin, ModelMap model) {
    Users user;
    ArrayList<Users> users;
    Rooms room = roomsMapper.selectById(roomId);
    Roles roles;
    user = usersMapper.selectByName(prin.getName());
    if (!room.isActive()) {
      return "close.html";
    }
    if (user.isDeath()) {
      return "death.html";
    }

    roles = rolesMapper.selectRoles(user.getRoles());
    model.addAttribute("roomName", room.getRoomName());
    model.addAttribute("roles", roles);
    users = usersMapper.selectAliveUsers(roomId);
    model.addAttribute("users", users);
    if ((-1) * room.getRoopCount() >= 1) {
      model.addAttribute("roopCount", room.getRoopCount() * (-1));
    }
    return "match.html";
  }

  @GetMapping("job")
  public String job(@RequestParam Integer id, Principal prin, ModelMap model) {
    Users loginUser = usersMapper.selectByName(prin.getName());
    Rooms room = roomsMapper.selectById(loginUser.getRoom());
    loginUser.setJobVote(id);
    usersMapper.updateJobVote(loginUser);
    loginUser.setKillVote(-1);
    usersMapper.updateKillVote(loginUser);
    model.addAttribute("roomId", loginUser.getRoom());
    model.addAttribute("userId", loginUser.getId());
    if (room.getRoopCount() < 0) {
      room.setRoopCount(((-1) * room.getRoopCount()) + 1);
      roomsMapper.updateRoopCount(room);
    }
    return "discWait.html";
  }

  @GetMapping("discuttion")
  public String discuttion(Principal prin, ModelMap model) {
    Users loginUser = usersMapper.selectByName(prin.getName());
    // Rooms room = roomsMapper.selectById(loginUser.getRoom());
    // loginUser.setJobVote(id);
    // usersMapper.updateJobVote(loginUser);
    // loginUser.setKillVote(-1);
    // usersMapper.updateKillVote(loginUser);
    if (!roomsMapper.selectById(loginUser.getRoom()).isActive()) {
      return "close.html";
    }
    if (loginUser.isDeath()) {
      return "death.html";
    }
    // if (room.getRoopCount() < 0) {
    // room.setRoopCount(((-1) * room.getRoopCount()) + 1);
    // roomsMapper.updateRoopCount(room);
    // }
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
      roomsMapper.updateRoopCount(room);
    }
    return "discFinish.html";
  }

  @GetMapping("result")
  public String result(@RequestParam Integer roomId, Principal prin, ModelMap model) {
    Users loginUser = usersMapper.selectByName(prin.getName());
    loginUser.setJobVote(-1);
    usersMapper.updateJobVote(loginUser);

    // 死ぬ人が決まっていない場合に処理
    if (killMaxIndex < 0) {
      System.out.println("test1");
      ArrayList<Users> users = usersMapper.selectAliveUsers(roomId);
      int[] vote = new int[10];
      System.out.println("test2");

      for (Users user : users) {
        vote[user.getKillVote() - 1]++;
      }
      System.out.println("test3");
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
    System.out.println("test4");
    if (killFlag) {
      Users deathUser = usersMapper.selectById(killMaxIndex + 1);
      model.addAttribute("death", deathUser);
      deathUser.setDeath(true);
      usersMapper.updateDeath(deathUser);
    }
    // 死ぬ人を決める処理
    System.out.println("test5");
    model.addAttribute("roomId", roomId);
    return "result.html";
  }

  @GetMapping("/wait-start")
  public SseEmitter waitStart() {
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

  @GetMapping("/wait-disc")
  public SseEmitter disc() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.gameService.syncCheckEntryDiscuttion(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("/wait-vote")
  public SseEmitter killVote() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.gameService.syncCheckKillVote(sseEmitter);
    return sseEmitter;
  }
}
