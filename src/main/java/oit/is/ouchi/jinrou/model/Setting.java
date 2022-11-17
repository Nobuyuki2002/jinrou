package oit.is.ouchi.jinrou.model;

public class Setting {
  int settingId;
  int startCount = 4;

  public Setting() {
  }

  public Setting(int startCount) {
    this.startCount = startCount;
  }

  public int getSettingId() {
    return settingId;
  }

  public void setSettingId(int settingId) {
    this.settingId = settingId;
  }

  public int getStartCount() {
    return startCount;
  }

  public void setStartCount(int startCount) {
    this.startCount = startCount;
  }


}
