package oit.is.ouchi.jinrou.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class LoginUser {
  private int userId;
  private String username;
  private String passwd;
  private String authorities;

  public LoginUser(String username, String passwd, String authorities) {
    this.username = username;
    this.passwd = passwd;
    this.authorities = authorities;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  public String getAuthorities() {
    return authorities;
  }

  public void setAuthorities(String authorities) {
    this.authorities = authorities;
  }

}
