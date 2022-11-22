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
import oit.is.ouchi.jinrou.model.UsersMapper;
//import oit.is.ouchi.jinrou.model.Users;

@Service
public class AsyncEntry {
  boolean dbUpdated = false;
  // int id;
  int startCount = 0;

  private final Logger logger = LoggerFactory.getLogger(AsyncEntry.class);

  // @Autowired
  // MatchMapper mMapper;

  @Autowired
  UsersMapper usersMapper;

  @Autowired
  RoomsMapper roomsMapper;

  public void setStartCount(int count) {
    this.startCount = count;
  }

  @Async
  public void syncCheckEntry(SseEmitter emitter) {
    //int cnt=0;
    int i = 1;
    dbUpdated = true;
    ArrayList<Rooms> rooms = new ArrayList<Rooms>();
    Rooms room = new Rooms();
    try {
        // DBが更新されていなければ0.5s休み
        // System.out.println("service"+(cnt++));
        // if (false == dbUpdated) {
        //   TimeUnit.MILLISECONDS.sleep(500);
        //   continue;
        // }
        // System.out.println("DBupdate");
        Count count = roomsMapper.selectCountRoom();
        Count startRoomCount;
        Count countPlayer;
        while (i <= count.getCount()) {
          startRoomCount = roomsMapper.selectStartRoomCount(i);
          countPlayer = usersMapper.selectCountByRoomId(i);
          if (startRoomCount.getCount() <= countPlayer.getCount()) {
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

  // @Transactional
  // public void insertUser(Users user){
  //   usersMapper.insertUsers(user);
  //   dbUpdated = true;
  // }

}
