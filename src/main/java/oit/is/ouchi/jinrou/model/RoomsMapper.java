package oit.is.ouchi.jinrou.model;

//import java.util.ArrayList;

//import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface RoomsMapper {

  @Select("select startCount as count from rooms natural join settings where roomId = #{roomId};")
  Count selectStartRoomCount(int roomId);

  @Select("select count(*) as count from rooms;")
  Count selectCountRoom();

  @Select("select * from rooms where roomId = #{roomId};")
  Rooms selectById(int roomId);

  @Select("select * from rooms where roomName = #{roomName} and isActive=TRUE")
  Rooms selectByName(String roomName);

  @Update("UPDATE rooms set wolfNum = #{wolfNum} WHERE roomId = #{roomId}")
  void updateWolfNum(Rooms room);

  @Update("UPDATE rooms set divinerNum = #{divinerNum} WHERE roomId = #{roomId}")
  void updateDivinerNum(Rooms room);

  @Update("UPDATE rooms SET roopCount = #{roopCount} WHERE roomId = #{roomId}")
  void updateRoopCount(Rooms room);

  @Update("UPDATE rooms SET roomName = #{roomName}, roomPass = #{roomPass}, roopCount = #{roopCount}, wolfNum = #{wolfNum}, winner = #{winner}, isActive = #{isActive} WHERE roomId = #{roomId};")
  void updateRoom(Rooms room);

  @Insert("INSERT INTO rooms (settingId, roomName, roomPass, roopCount, wolfNum, winner ,isActive) VALUES (#{settingId}, #{roomName}, #{roomPass}, -1, 0, 0, TRUE);")
  @Options(useGeneratedKeys = true, keyColumn = "roomId", keyProperty = "roomId")
  void insertRooms(Rooms room);

}
