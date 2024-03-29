package com.striveonger.study.task.core.scope.trigger;

import com.striveonger.study.task.common.scope.context.PerformParam;
import com.striveonger.study.task.common.scope.context.storage.ContextStorage;
import com.striveonger.study.task.core.executor.extra.ExecutorExtraInfo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description: 任务触发器
 * @date 2023-05-04 15:35
 */
public class TaskTrigger {
    /**
     * taskID
     */
    private String taskID;

    /**
     * task 执行参数
     */
    private final List<PerformParam> params = new ArrayList<>();

    /**
     * task 执行列表 (包含扩展信息)
     */
    private final Map<String, ExecutorExtraInfo> extras = new ConcurrentHashMap<>();

    /**
     * task 执行顺序 (用邻接表表示的拓扑序)
     * stepID -> [nextStepID...]
     */
    private Map<String, Set<String>> topology;

    private ContextStorage storage;

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public List<PerformParam> getParams() {
        return params;
    }

    public void putParam(PerformParam param) {
        params.add(param);
    }

    public void putAllParam(Collection<PerformParam> list) {
        params.addAll(list);
    }

    public ExecutorExtraInfo getExtra(String key) { return extras.get(key); }

    public Collection<ExecutorExtraInfo> getExtras() {
        return extras.values();
    }

    public Map<String, ExecutorExtraInfo> getExtraMap() { return extras; }

    public void putExtra(ExecutorExtraInfo extra) {
        this.extras.put(extra.getStepID(), extra);
    }

    public void putAllExtra(Collection<ExecutorExtraInfo> list) {
        for (ExecutorExtraInfo extra : list) {
            this.extras.put(extra.getStepID(), extra);
        }
    }

    public Map<String, Set<String>> getTopology() {
        return topology;
    }

    public void setTopology(Map<String, Set<String>> topology) {
        this.topology = topology;
    }

    public ContextStorage getStorage() {
        return storage;
    }

    public void setStorage(ContextStorage storage) {
        this.storage = storage;
    }
}
