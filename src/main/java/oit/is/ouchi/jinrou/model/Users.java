package oit.is.ouchi.jinrou.model;

public class Users {
  int id;
  String lname;
  String pname;
  int room;
  int roles;
  boolean isDeath;
  int jobVote;
  int killVote;

  public Users() {
  }

  public Users(String lname, String pname, int room, int roles) {
    this.lname = lname;
    this.pname = pname;
    this.room = room;
    this.isDeath = false;
    this.roles = roles;
    this.jobVote = -1;
    this.killVote = -1;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPname() {
    return pname;
  }

  public void setPname(String pname) {
    this.pname = pname;
  }

  public String getLname() {
    return lname;
  }

  public void setLname(String lname) {
    this.lname = lname;
  }

  public int getRoom() {
    return room;
  }

  public void setRoom(int room) {
    this.room = room;
  }

  public int getRoles() {
    return roles;
  }

  public void setRoles(int roles) {
    this.roles = roles;
  }

  public boolean isDeath() {
    return isDeath;
  }

  public void setDeath(boolean isDeath) {
    this.isDeath = isDeath;
  }

  public int getJobVote() {
    return jobVote;
  }

  public void setJobVote(int jobVote) {
    this.jobVote = jobVote;
  }

  public int getKillVote() {
    return killVote;
  }

  public void setKillVote(int killVote) {
    this.killVote = killVote;
  }

}
