package oit.is.ouchi.jinrou.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UsersMapper {

  @Insert("INSERT INTO users (lname,pname,room,roles,isdeath) VALUES (#{lname},#{pname},#{room},#{roles},#{vote});")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertUsers(Users user);

  @Select("select * from users where lname = #{lname}")
  Users selectByName(String lname);

  @Select("SELECT COUNT(room) as count FROM users WHERE room = #{room_id}")
  Count selectCountByRoomId(int room_id);

  @Select("SELECT * FROM users;")
  ArrayList<Users> selectAll();
}
