package oit.is.ouchi.jinrou.service;

import java.util.ArrayList;

import org.apache.tomcat.jni.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.ouchi.jinrou.model.Count;
import oit.is.ouchi.jinrou.model.Rooms;
import oit.is.ouchi.jinrou.model.RoomsMapper;
import oit.is.ouchi.jinrou.model.Users;
import oit.is.ouchi.jinrou.model.UsersMapper;
//import oit.is.ouchi.jinrou.model.Users;

@Service
public class AsyncGameThread {

  private final Logger logger = LoggerFactory.getLogger(AsyncEntry.class);

  @Autowired
  UsersMapper usersMapper;

  @Autowired
  RoomsMapper roomsMapper;

  @Async
  public void syncCheckEntryDiscuttion(SseEmitter emitter) {
    // int cnt=0;
    int i = 1;
    ArrayList<Rooms> rooms = new ArrayList<Rooms>();
    Rooms room;
    try {
      Count count = roomsMapper.selectCountRoom();
      int aliveCount;
      Count countPlayer;
      while (i <= count.getCount()) {
        aliveCount = usersMapper.selectAliveUsers(i).size();
        countPlayer = usersMapper.selectJobVoteCount(i);
        if (aliveCount <= countPlayer.getCount()) {
          ArrayList<Users> aliveUsersInRoom = usersMapper.selectAliveUsers(i);
          // 人狼が2日目以降、誰かを殺害する
          for (Users alive : aliveUsersInRoom) {
            if (alive.getRoles() == 2 && roomsMapper.selectById(i).getRoopCount() > 2) {
              Users killed = usersMapper.selectById(alive.getJobVote());
              killed.setDeath(true);
              usersMapper.updateDeath(killed);
              break;
            }
            if (alive.getRoles() == 2 && roomsMapper.selectById(i).getRoopCount() * (-1) > 2) {
              Users killed = usersMapper.selectById(alive.getJobVote());
              killed.setDeath(true);
              usersMapper.updateDeath(killed);
              break;
            }
          }
          gameJudge(i);
          room = roomsMapper.selectById(i);
          rooms.add(room);
        }
        i++;
      }
      emitter.send(rooms);
      // dbUpdated = false;
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("AsyncEntry complete");
  }

  @Async
  public void syncCheckJobVote(SseEmitter emitter) {

    int i = 1;
    ArrayList<Users> aliveUsers = new ArrayList<Users>();
    ArrayList<Users> aliveUsersInRoom = new ArrayList<Users>();
    try {
      Count count = roomsMapper.selectCountRoom();
      int aliveCount;
      Count countPlayer;
      while (i <= count.getCount()) {
        aliveCount = usersMapper.selectAliveUsers(i).size();
        countPlayer = usersMapper.selectJobVoteCount(i);
        if (aliveCount <= countPlayer.getCount()) {
          aliveUsersInRoom = usersMapper.selectAliveUsers(i);
          for (Users alive : aliveUsersInRoom) {
            aliveUsers.add(alive);
          }
        }
        i++;
      }
      emitter.send(aliveUsers);
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("AsyncEntry complete");
  }

  @Async
  public void syncCheckKillVote(SseEmitter emitter) {

    int i = 1;
    ArrayList<Rooms> rooms = new ArrayList<Rooms>();
    Rooms room = new Rooms();
    try {
      Count count = roomsMapper.selectCountRoom();
      int aliveCount;
      Count countPlayer;
      while (i <= count.getCount()) {
        aliveCount = usersMapper.selectAliveUsers(i).size();
        countPlayer = usersMapper.selectKillVoteCount(i);
        if (aliveCount <= countPlayer.getCount()) {
          room = roomsMapper.selectById(i);
          gameJudge(room.getRoomId());
          rooms.add(room);
        }
        i++;
      }
      emitter.send(rooms);
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("AsyncEntry complete");
  }

  @Transactional
  public void gameJudge(int roomId) {
    System.out.println("gameJadge");
    System.out.println(usersMapper.selectCountByRoleId(roomId, 1, false).getCount());
    Rooms room = roomsMapper.selectById(roomId);
    // ゲーム続行判定(村人が1人以下になれば人狼対村人が1:1)
    if (usersMapper.selectCountByRoleId(roomId, 1, false).getCount() <= 1) {
      System.out.println("gameJudge1");
      room.setActive(false);
      room.setRoopCount(-1);
      room.setWolfNum(0);
      room.setWinner(2);
      roomsMapper.updateRoom(room);
    }
    if (usersMapper.selectCountByRoleId(roomId, 2, false).getCount() < 1) {
      System.out.println("gameJudge2");
      room.setActive(false);
      room.setRoopCount(-1);
      room.setWolfNum(0);
      room.setWinner(1);
      roomsMapper.updateRoom(room);
    }
  }
}
