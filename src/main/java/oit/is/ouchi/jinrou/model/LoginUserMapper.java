package oit.is.ouchi.jinrou.model;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginUserMapper {

  @Insert("INSERT INTO loginUsers ( username, passwd, authorities ) VALUES (#{username}, #{passwd}, #{authorities} );")
  @Options(useGeneratedKeys = true, keyColumn = "userId", keyProperty = "userId")
  void insertLoginUser(LoginUser user);

  @Select("SELECT * FROM loginUsers WHERE username = #{username}")
  LoginUser selectLoginUserByName(String username);

}
