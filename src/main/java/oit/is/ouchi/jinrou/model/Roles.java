package oit.is.ouchi.jinrou.model;

//import javax.management.relation.Role;

public class Roles {
  int rolId;
  String rolName;
  boolean isVillager;

  public Roles() {
  }

  public int getRolId() {
    return rolId;
  }

  public void setRolId(int rolId) {
    this.rolId = rolId;
  }

  public String getRolName() {
    return rolName;
  }

  public void setRolName(String rolName) {
    this.rolName = rolName;
  }

  public boolean isVillager() {
    return isVillager;
  }

  public void setVillager(boolean isVillager) {
    this.isVillager = isVillager;
  }
}
