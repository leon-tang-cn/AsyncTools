package com.polarwind.thread.async.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.polarwind.thread.async.model.BaseRelationModel;
import com.polarwind.thread.async.model.BaseWorkerModel;
import com.polarwind.thread.async.worker.AbstractWorker;
import com.polarwind.thread.async.worker.WorkResult;
import com.polarwind.thread.async.wrapper.WorkerWrapper;
import com.polarwind.thread.async.wrapper.WorkerWrapper.Builder;

public class WorkerWrapperBuilder {

  public static WorkerWrapper<Map<String, Object>, Map<String, Object>> createWorkerWrapper(
    List<? extends BaseWorkerModel> workers, List<? extends BaseRelationModel> relations, List<Long> firstLvlIds,
    Class<? extends AbstractWorker> workerClass) throws InstantiationException, IllegalAccessException {

    Builder<Map<String, Object>, Map<String, Object>> startWrapperBuilder = createBuilderInstance(false, workerClass);

    Map<Long, Builder<Map<String, Object>, Map<String, Object>>> allBuilders = new HashMap<>();

    // 存放所有的wrapper
    Map<Long, WorkerWrapper<Map<String, Object>, Map<String, Object>>> allWrapperMap = new HashMap<>();

    // 给所有的job创建wrapperBuilder
    for (BaseWorkerModel worker : workers) {
      allBuilders.put(worker.getJobId(), createBuilderInstance(worker.getAllowInterrupt(), workerClass));
    }

    // 给虚拟的startWrapper设置下一级wrapper
    for (Long firstLvlJobId : firstLvlIds) {
      startWrapperBuilder.next(createWrapperInstance(firstLvlJobId, allWrapperMap, workers, allBuilders));
    }
    // 给wrapper设置入参
    setWrapperParam(workers, relations, allWrapperMap);
    WorkerWrapper<Map<String, Object>, Map<String, Object>> startWrapper = startWrapperBuilder.build();
    startWrapper.setParam(new HashMap<String, Object>());
    return startWrapper;
  }

  private static Builder<Map<String, Object>, Map<String, Object>> createBuilderInstance(Boolean allowInterrupt,
    Class<? extends AbstractWorker> workerClass) throws InstantiationException, IllegalAccessException {
    AbstractWorker currentWorker = workerClass.newInstance();
    return new WorkerWrapper.Builder<Map<String, Object>, Map<String, Object>>().worker(currentWorker)
      .callback(currentWorker).allowInterrupt(allowInterrupt);
  }

  private static WorkerWrapper<Map<String, Object>, Map<String, Object>> createWrapperInstance(Long jobId,
    Map<Long, WorkerWrapper<Map<String, Object>, Map<String, Object>>> allWrapperMap,
    List<? extends BaseWorkerModel> workers, Map<Long, Builder<Map<String, Object>, Map<String, Object>>> allBuilders) {
    List<? extends BaseWorkerModel> childJobs = workers.stream()
      .filter(s -> null != s.getPreJobId() && s.getPreJobId().equals(jobId)).collect(Collectors.toList());
    WorkerWrapper<Map<String, Object>, Map<String, Object>> wrapper = null;
    if (!Optional.of(childJobs).isPresent() || childJobs.isEmpty()) {
      wrapper = allBuilders.get(jobId).build();
      allWrapperMap.put(jobId, wrapper);
      return wrapper;
    } else {
      Builder<Map<String, Object>, Map<String, Object>> currentBuilder = allBuilders.get(jobId);
      for (BaseWorkerModel childJob : childJobs) {
        if (allWrapperMap.containsKey(childJob.getJobId())) {
          currentBuilder.next(allWrapperMap.get(childJob.getJobId()));
        } else {
          currentBuilder.next(createWrapperInstance(childJob.getJobId(), allWrapperMap, workers, allBuilders));
        }
      }
      wrapper = currentBuilder.build();
      allWrapperMap.put(jobId, wrapper);
      return wrapper;
    }
  }

  private static void setWrapperParam(List<? extends BaseWorkerModel> workers,
    List<? extends BaseRelationModel> relations,
    Map<Long, WorkerWrapper<Map<String, Object>, Map<String, Object>>> allWrapperMap) {
    workers.forEach(worker -> {
      Map<String, Object> param = new HashMap<>();
      param.put(AbstractWorker.getInstanceParamName(), worker);
      if (Optional.of(relations).isPresent()) {
        // 获取当前job的所有前置jobId
        List<Long> preJobIds =
          relations.stream().filter(s -> null != s.getPreJobId() && s.getJobId().equals(worker.getJobId()))
            .map(BaseRelationModel::getPreJobId).collect(Collectors.toList());
        if (Optional.of(preJobIds).isPresent() && !preJobIds.isEmpty()) {
          // 取出所有的上一级wrapper的result塞给当前的wrapper
          List<WorkResult<Map<String, Object>>> results = new ArrayList<>();
          preJobIds.forEach(id -> {
            results.add(allWrapperMap.get(id).getWorkResult());
          });
          param.put(AbstractWorker.getResultsParamName(), results);
        }
      }
      allWrapperMap.get(worker.getJobId()).setParam(param);
    });
  }
}
