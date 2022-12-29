package oit.is.ouchi.jinrou.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import oit.is.ouchi.jinrou.model.UserDetailsImp;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      String sql = "SELECT * FROM loginUsers WHERE username = ?";
      Map<String, Object> map = jdbcTemplate.queryForMap(sql, username);
      String passwd = (String) map.get("passwd");
      Collection<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority((String) map.get("authorities")));
      return new UserDetailsImp(username, passwd, authorities);
    } catch (Exception ex) {
      throw new UsernameNotFoundException("user not found.", ex);
    }
  }
}
