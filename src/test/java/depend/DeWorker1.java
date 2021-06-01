package depend;


import java.util.Map;

import com.polarwind.thread.async.callback.ICallback;
import com.polarwind.thread.async.callback.IWorker;
import com.polarwind.thread.async.worker.WorkResult;
import com.polarwind.thread.async.wrapper.WorkerWrapper;

/**
 * @author wuweifeng wrote on 2019-11-20.
 */
public class DeWorker1 implements IWorker<WorkResult<User>, User>, ICallback<WorkResult<User>, User> {

    @Override
  public User action(WorkResult<User> result, Map<String, WorkerWrapper<?, ?>> allWrappers) {
        System.out.println("par1的入参来自于par0： " + result.getResult());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new User("user1");
    }


    @Override
    public User defaultValue() {
        return new User("default User");
    }

    @Override
    public void begin() {
        //System.out.println(Thread.currentThread().getName() + "- start --" + System.currentTimeMillis());
    }

    @Override
    public void result(boolean success, WorkResult<User> param, WorkResult<User> workResult) {
        System.out.println("worker1 的结果是：" + workResult.getResult());
    }

}
