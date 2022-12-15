package oit.is.ouchi.jinrou.model;

public class Vote {
  private int killMaxIndex;
  private Boolean killFlag;
  private int jobMaxIndex;
  private int jobVoteUserId;
  private int killVoteUserId;

  public Vote() {
    this.killMaxIndex = -1;
    this.killFlag = true;
    this.jobMaxIndex = -1;
  }

  public int getKillMaxIndex() {
    return killMaxIndex;
  }

  public void setKillMaxIndex(int killMaxIndex) {
    this.killMaxIndex = killMaxIndex;
  }

  public Boolean getKillFlag() {
    return killFlag;
  }

  public void setKillFlag(Boolean killFlag) {
    this.killFlag = killFlag;
  }

  public int getJobMaxIndex() {
    return jobMaxIndex;
  }

  public void setJobMaxIndex(int jobMaxIndex) {
    this.jobMaxIndex = jobMaxIndex;
  }

  public int getJobVoteUserId() {
    return jobVoteUserId;
  }

  public void setJobVoteUserId(int jobVoteUserId) {
    this.jobVoteUserId = jobVoteUserId;
  }

  public int getKillVoteUserId() {
    return killVoteUserId;
  }

  public void setKillVoteUserId(int killVoteUserId) {
    this.killVoteUserId = killVoteUserId;
  }

}
