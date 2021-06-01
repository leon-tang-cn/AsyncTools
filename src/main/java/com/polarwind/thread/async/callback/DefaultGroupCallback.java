package com.polarwind.thread.async.callback;

import java.util.List;

import com.polarwind.thread.async.wrapper.WorkerWrapper;

/**
 * @author wuweifeng wrote on 2019-12-27
 * @version 1.0
 */
public class DefaultGroupCallback implements IGroupCallback {
  @Override
  public void success(List<WorkerWrapper<?, ?>> workerWrappers) {

  }

  @Override
  public void failure(List<WorkerWrapper<?, ?>> workerWrappers, Exception e) {

  }
}
