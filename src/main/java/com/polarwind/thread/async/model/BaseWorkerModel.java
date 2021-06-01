package com.polarwind.thread.async.model;

public class BaseWorkerModel {
  // 作业id
  private Long jobId;

  private Long preJobId;

  private Boolean allowInterrupt;

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

  public Boolean getAllowInterrupt() {
    return allowInterrupt;
  }

  public void setAllowInterrupt(Boolean allowInterrupt) {
    this.allowInterrupt = allowInterrupt;
  }
}
