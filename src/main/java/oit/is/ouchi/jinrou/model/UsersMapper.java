package oit.is.ouchi.jinrou.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UsersMapper {

  @Insert("INSERT INTO users (room,pname,roles,isdeath) VALUES (1,#{pname},1,true);")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertUsers(Users user);

  @Select("select * from users where pname = #{pname}")
  Users selectByName(String pname);

  @Update("UPDATE users SET pname=#{pname} where id = #{id}")
  void updateById(int id, String pname);

}
