package com.polarwind.thread.async.model;

public class BaseRelationModel {

  // 关系id
  private Long relId;

  // 作业id
  private Long jobId;

  // 前置id
  private Long preJobId;

  private Long jobLevel;

  public Long getRelId() {
    return relId;
  }

  public void setRelId(Long relId) {
    this.relId = relId;
  }

  public Long getJobId() {
    return jobId;
  }

  public void setJobId(Long jobId) {
    this.jobId = jobId;
  }

  public Long getPreJobId() {
    return preJobId;
  }

  public void setPreJobId(Long preJobId) {
    this.preJobId = preJobId;
  }

  public Long getJobLevel() {
    return jobLevel;
  }

  public void setJobLevel(Long jobLevel) {
    this.jobLevel = jobLevel;
  }

}
