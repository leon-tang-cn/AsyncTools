package com.polarwind.thread.async.worker;

import java.util.Map;

import com.polarwind.thread.async.callback.ICallback;
import com.polarwind.thread.async.callback.IWorker;
import com.polarwind.thread.async.wrapper.WorkerWrapper;

public abstract class AbstractWorker
  implements IWorker<Map<String, Object>, Map<String, Object>>, ICallback<Map<String, Object>, Map<String, Object>> {

  @Override
  public abstract Map<String, Object> action(Map<String, Object> paramMap,
    Map<String, WorkerWrapper<?, ?>> allWrappers);

  @Override
  public abstract Map<String, Object> defaultValue();

  @Override
  public abstract void begin();

  @Override
  public abstract void result(boolean success, Map<String, Object> param, WorkResult<Map<String, Object>> workResult);

  public static String getInstanceParamName() {
    return "jobModel";
  }

  public static String getResultsParamName() {
    return "results";
  }
}
