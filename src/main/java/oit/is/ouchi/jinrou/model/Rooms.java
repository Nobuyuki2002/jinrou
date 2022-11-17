package oit.is.ouchi.jinrou.model;

public class Rooms {
  int roomId;
  int settingId;
  String roomName;
  int roomPass;
  int roopCount;
  int wolfNum;
  int winner; // 1 : 村人陣営 2 : 人狼陣営 0 : 勝者未定
  boolean isActive;

  public Rooms() {
  }

  public Rooms(int settingId, String roomName, int roomPass) {
    this.settingId = settingId;
    this.roomName = roomName;
    this.roomPass = roomPass;
    this.roopCount = 0;
    this.wolfNum = 0;
    this.winner = 0;
    this.isActive = true;
  }

  public int getWolfNum() {
    return wolfNum;
  }

  public void setWolfNum(int wolfNum) {
    this.wolfNum = wolfNum;
  }

  public int getSettingId() {
    return settingId;
  }

  public void setSettingId(int settingId) {
    this.settingId = settingId;
  }

  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public int getRoomPass() {
    return roomPass;
  }

  public void setRoomPass(int roomPass) {
    this.roomPass = roomPass;
  }

  public int getRoopCount() {
    return roopCount;
  }

  public void setRoopCount(int roopCount) {
    this.roopCount = roopCount;
  }

  public int getWinner() {
    return winner;
  }

  public void setWinner(int winner) {
    this.winner = winner;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public int getRoomId() {
    return roomId;
  }

  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }

}
