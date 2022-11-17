package oit.is.ouchi.jinrou.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RoomsMapper {

  @Select("select startCount as count from rooms natural join settings where roomId = #{roomId};")
  Count selectStartRoomCount(int roomId);

  @Select("select count(*) as count from rooms;")
  Count selectCountRoom();

  @Select("select * from rooms where roomId = #{roomId};")
  Rooms selectById(int roomId);

}
