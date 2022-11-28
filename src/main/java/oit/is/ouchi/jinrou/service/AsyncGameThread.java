package oit.is.ouchi.jinrou.service;

import java.util.ArrayList;

import org.apache.tomcat.jni.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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

}
