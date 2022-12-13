package oit.is.ouchi.jinrou.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Select;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.ui.Model;
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

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.io.IOException;

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

  int jobMaxIndex = -1;

  @GetMapping("/")
  public String jinrou(ModelMap model, Principal prin) {
    Users tmp = null;
    if ((tmp = usersMapper.selectByName(prin.getName())) == null) {
      return "createUser.html";
    }

    if (!roomsMapper.selectById(tmp.getRoom()).isActive()) {
      return "createUser.html";
    }

    if (tmp.getRoom() < 0) {
      return "matching.html";
    }
    model.addAttribute("roomId", tmp.getRoom());
    model.addAttribute("userName", tmp.getPname());
    return "gameWait.html";
  }

  @PostMapping("make")
  @Transactional
  public String make(@RequestParam String roomName, @RequestParam String roomPass, ModelMap model, Principal prin) {
    Rooms room = new Rooms(1, roomName, roomPass);
    Users user = usersMapper.selectByName(prin.getName());

    if ((roomsMapper.selectByName(roomName)) == null) {
      roomsMapper.insertRooms(room);
      room = roomsMapper.selectByName(roomName);
      user.setRoom(room.getRoomId());
      usersMapper.updateRoomId(user);
      model.addAttribute("roomId", room.getRoomId());
      System.out.println(room.getRoomId());
      model.addAttribute("userName", user.getPname());
      return "gameWait.html";
    }
    return "matching.html";
  }

  @PostMapping("join")
  @Transactional
  public String join(@RequestParam String roomName, @RequestParam String roomPass, ModelMap model, Principal prin) {
    Rooms room;
    Users user = usersMapper.selectByName(prin.getName());

    if ((room = roomsMapper.selectByName(roomName)) == null || !roomPass.equals(room.getRoomPass())) {
      return "matching.html";
    }

    user.setRoom(room.getRoomId());
    usersMapper.updateRoomId(user);
    model.addAttribute("roomId", room.getRoomId());
    model.addAttribute("userName", user.getPname());
    return "gameWait.html";

  }

  @PostMapping("entry")
  @Transactional
  public String entry(@RequestParam String pname, ModelMap model, Principal prin) {
    Users user = new Users(prin.getName(), pname, -1, 3);
    Users tmp = new Users();
    if ((tmp = usersMapper.selectByName(pname)) == null) {
      usersMapper.insertUsers(user);
      return "matching.html";
    }
    if (!roomsMapper.selectById(tmp.getRoom()).isActive()) {
      return "createUser.html";
    }
    if (tmp.getRoom() < 0) {
      return "matching.html";
    }

    model.addAttribute("roomId", tmp.getRoom());
    model.addAttribute("userName", tmp.getPname());

    return "gameWait.html";
  }

  @GetMapping("match")
  @Transactional
  public String match(@RequestParam Integer roomId, Principal prin, ModelMap model, Model models) {
    Users user;
    ArrayList<Users> users;
    Rooms room = roomsMapper.selectById(roomId);
    Roles roles;
    if (!room.isActive()) {
      switch (room.getWinner()) {
        case 1:
          user = usersMapper.selectByName(prin.getName());
          usersMapper.updateLnameByName(user);
          model.addAttribute("winner", "村人陣営");
          break;
        case 2:
          user = usersMapper.selectByName(prin.getName());
          usersMapper.updateLnameByName(user);
          model.addAttribute("winner", "人狼陣営");
          break;
      }
      return "close.html";
    }
    user = usersMapper.selectByName(prin.getName());
    if (user.isDeath()) {
      this.ImageView("death.jpg", models, "deathImg");
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

    // TODO:役職ごとに画像を変える
    this.ImageView("wolf.jpg", models, "wolfImg");

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
    Rooms room = roomsMapper.selectById(loginUser.getRoom());

    if (!room.isActive()) {
      switch (room.getWinner()) {
        case 1:
          usersMapper.updateLnameByName(loginUser);
          model.addAttribute("winner", "村人陣営");
          break;
        case 2:
          usersMapper.updateLnameByName(loginUser);
          model.addAttribute("winner", "人狼陣営");
          break;
      }
      return "close.html";
    }
    if (loginUser.isDeath()) {
      return "death.html";
    }

    // 疑わしい人が決まっていない場合に処理
    if (jobMaxIndex < 0) {
      ArrayList<Users> users = usersMapper.selectAliveUsers(room.getRoomId());
      int[] vote = new int[15];

      for (Users user : users) {
        vote[user.getJobVote() - 1]++;
      }

      int max = vote[0];
      jobMaxIndex = 0;
      for (int i = 1; i < vote.length; i++) {
        if (max <= vote[i]) {
          jobMaxIndex = i;
          max = vote[i];
        }
      }
    }

    Users suspicious = usersMapper.selectById(jobMaxIndex + 1);
    model.addAttribute("suspicious", suspicious);

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
  public String result(@RequestParam Integer roomId, Principal prin, ModelMap model, Model models) {
    Users loginUser = usersMapper.selectByName(prin.getName());
    loginUser.setJobVote(-1);
    usersMapper.updateJobVote(loginUser);

    // 死ぬ人が決まっていない場合に処理
    if (killMaxIndex < 0) {
      ArrayList<Users> users = usersMapper.selectAliveUsers(roomId);
      int[] vote = new int[10];

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
          if (max != 0) {
            killFlag = false;
            break;
          }
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

    if (killFlag) {
      this.ImageView("rope.png", models, "ropeImg");
    }

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

  public void ImageView(String imageFile, Model model, String bookName) {
    final String imgPath = "./src/main/java/oit/is/ouchi/jinrou/img/";
    File file = new File(imgPath + imageFile);

    try {
      byte[] imageByte = Files.readAllBytes(file.toPath());
      String base64Data = Base64.getEncoder().encodeToString(imageByte);
      model.addAttribute(bookName, "data:image/jpg;base64," + base64Data);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
