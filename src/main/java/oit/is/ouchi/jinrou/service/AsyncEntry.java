package oit.is.ouchi.jinrou.service;

import java.util.ArrayList;
//import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.ouchi.jinrou.model.Count;
import oit.is.ouchi.jinrou.model.Rooms;
import oit.is.ouchi.jinrou.model.RoomsMapper;
import oit.is.ouchi.jinrou.model.Users;
import oit.is.ouchi.jinrou.model.UsersMapper;
//import oit.is.ouchi.jinrou.model.Users;

@Service
public class AsyncEntry {
  boolean dbUpdated = false;
  // int id;

  private final Logger logger = LoggerFactory.getLogger(AsyncEntry.class);

  // @Autowired
  // MatchMapper mMapper;

  @Autowired
  UsersMapper usersMapper;

  @Autowired
  RoomsMapper roomsMapper;

  @Async
  public void syncCheckEntry(SseEmitter emitter) {
    // int cnt=0;
    int i = 1;
    ArrayList<Rooms> rooms = new ArrayList<Rooms>();
    Rooms room;
    try {
      Count count = roomsMapper.selectCountRoom();
      Count startRoomCount;
      Count countPlayer;
      while (i <= count.getCount()) {
        if (!roomsMapper.selectById(i).isActive()) {
          i++;
          continue;
        }
        // ルーム人数制限取得
        startRoomCount = roomsMapper.selectStartRoomCount(i);
        // ルーム人数取得
        countPlayer = usersMapper.selectCountByRoomId(i);
        ArrayList<Users> roomUsers = usersMapper.selectByRoomId(i);
        // ルーム人数制限以上である場合
        if (startRoomCount.getCount() <= countPlayer.getCount()) {
          room = roomsMapper.selectById(i);
          rooms.add(room);
          Count countRoomMember = usersMapper.selectCountByRoomId(i);
          int wolfNum = room.getWolfNum();
          if (wolfNum == 0) {
            wolfNum = (int) (Math.random() * countRoomMember.getCount()) + 1;
            room.setWolfNum(roomUsers.get(wolfNum).getId());
            roomsMapper.updateWolfNum(room);
            ArrayList<Users> roomMember = usersMapper.selectByRoomId(i);
            for (Users tmp : roomMember) {
              if (wolfNum == tmp.getId()) {
                tmp.setRoles(2);
              } else {
                tmp.setRoles(1);
              }
              usersMapper.updateRole(tmp);
            }
          }
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

  // @Transactional
  // public void insertUser(Users user){
  // usersMapper.insertUsers(user);
  // dbUpdated = true;
  // }

}
