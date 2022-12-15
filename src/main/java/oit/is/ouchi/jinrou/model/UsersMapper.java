package oit.is.ouchi.jinrou.model;

import java.util.ArrayList;

import org.apache.catalina.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UsersMapper {

  @Insert("INSERT INTO users (lname,pname,room,roles,isDeath,isDivined,jobVote,killVote) VALUES (#{lname},#{pname},#{room},#{roles},#{isDeath},#{isDivined},#{jobVote},#{killVote});")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertUsers(Users user);

  @Select("select * from users where isDivined = true")
  ArrayList<Users> selectByDivinedUsers();

  @Select("select * from users where lname = #{lname}")
  Users selectByName(String lname);

  @Select("select * from users where id = #{id}")
  Users selectById(int id);

  @Select("select * from users where room = #{room} and isDeath = false")
  ArrayList<Users> selectAliveUsers(int room);

  @Select("SELECT COUNT(room) as count FROM users WHERE room = #{roomId}")
  Count selectCountByRoomId(int roomId);

  @Select("SELECT * FROM users;")
  ArrayList<Users> selectAll();

  @Select("SELECT * FROM users WHERE room = #{roomId};")
  ArrayList<Users> selectByRoomId(int roomId);

  @Update("UPDATE users set  roles = #{roles} WHERE id = #{id}")
  void updateRole(Users user);

  @Update("UPDATE users set jobVote = #{jobVote} WHERE id = #{id};")
  void updateJobVote(Users user);

  @Update("UPDATE users set killVote = #{killVote} WHERE id = #{id};")
  void updateKillVote(Users user);

  @Update("UPDATE users SET isDeath = #{isDeath} WHERE id = #{id};")
  void updateDeath(Users user);

  @Update("UPDATE users SET isDivined = #{isDivined} WHERE id = #{id};")
  void updateDivineFlag(Users user);

  @Update("UPDATE users SET room = #{room} WHERE id = #{id};")
  void updateRoomId(Users user);

  @Select("SELECT COUNT(*) as count FROM users WHERE jobVote >= 0 and room = #{room};")
  Count selectJobVoteCount(int room);

  @Select("SELECT COUNT(*) as count FROM users WHERE killVote >= 0 and room = #{room};")
  Count selectKillVoteCount(int room);

  @Select("SELECT COUNT(*) as count FROM users WHERE room = #{room} and roles = #{roles} and isDeath = #{isDeath};")
  Count selectCountByRoleId(int room, int roles, boolean isDeath);

  @Select("SELECT COUNT(*) as count FROM users WHERE room = #{room} and roles != #{roles} and isDeath = #{isDeath};")
  Count selectCountByVillagerId(int room, int roles, boolean isDeath);

  @Delete("DELETE FROM users WHERE room = #{room};")
  void deleteUsersByRoom(int room);

  @Update("UPDATE users set lname = NULL WHERE id = #{id};")
  void updateLnameByName(Users user);

  @Select("SELECT COUNT(*) as count FROM users WHERE jobVote = #{id} and isDeath = FALSE;")
  Count selectJobVoteCountById(Users user);

  @Select("SELECT COUNT(*) as count FROM users WHERE killVote = #{id} and isDeath = FALSE;")
  Count selectKillVoteCountById(Users user);
}
