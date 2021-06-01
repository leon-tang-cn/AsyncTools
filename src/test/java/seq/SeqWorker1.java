package seq;

import java.util.Map;

import com.polarwind.thread.async.callback.ICallback;
import com.polarwind.thread.async.callback.IWorker;
import com.polarwind.thread.async.executor.timer.SystemClock;
import com.polarwind.thread.async.worker.WorkResult;
import com.polarwind.thread.async.wrapper.WorkerWrapper;

/**
 * @author wuweifeng wrote on 2019-11-20.
 */
public class SeqWorker1 implements IWorker<String, String>, ICallback<String, String> {

  @Override
  public String action(String object, Map<String, WorkerWrapper<?, ?>> allWrappers) {
    try {
      for (int i = 0; i < 10; i++) {
        Thread.sleep(1000);
        System.out.println("looper-" + i);
      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "result = " + SystemClock.now() + "---param = " + object + " from 0";
  }

  @Override
  public String defaultValue() {
    return "worker1--default";
  }

  @Override
  public void begin() {
    // System.out.println(Thread.currentThread().getName() + "- start --" + System.currentTimeMillis());
  }

  @Override
  public void result(boolean success, String param, WorkResult<String> workResult) {
    if (success) {
      System.out.println("callback worker1 success--" + SystemClock.now() + "----" + workResult.getResult()
        + "-threadName:" + Thread.currentThread().getName());
    } else {
      System.err.println("callback worker1 failure--" + SystemClock.now() + "----" + workResult.getResult()
        + "-threadName:" + Thread.currentThread().getName());
    }
  }

}
