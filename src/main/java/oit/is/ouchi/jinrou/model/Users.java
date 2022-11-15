package oit.is.ouchi.jinrou.model;

public class Users {
  int id;
  int room;
  String pname;
  int roles;
  boolean isDeath;
  int vote;

  public Users(int room, String pname, int roles, boolean isDeath, int vote) {
    this.room = room;
    this.pname = pname;
    this.isDeath = isDeath;
    this.roles = roles;
    this.vote = vote;
  }

  public String getPname() {
    return pname;
  }

  public void setUserName(String pname) {
    this.pname = pname;
  }

  public int getRoles() {
    return roles;
  }

  public void setRoles(int roles) {
    this.roles = roles;
  }

  public boolean getIsDeath() {
    return isDeath;
  }

  public void setRoles(boolean isDeath) {
    this.isDeath = isDeath;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getVote() {
    return vote;
  }

  public void setVote(int vote) {
    this.vote = vote;
  }

  public int getRoom() {
    return room;
  }

  public void setRoom(int room) {
    this.room = room;
  }

}
