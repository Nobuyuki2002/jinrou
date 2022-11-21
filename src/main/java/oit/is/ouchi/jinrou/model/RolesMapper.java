package oit.is.ouchi.jinrou.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RolesMapper {
  @Select("SELECT * FROM roles WHERE ROLID = #{rolId};")
  Roles selectRoles(int rolId);
}
