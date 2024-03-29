package com.striveonger.study.task.executor.adapter;

import com.striveonger.study.task.common.executor.step.adapter.StepAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-21 11:52
 */
public class AdapterLoader {
    /**
     * 每个组件负责自己的适配器
     * 这就负责去加载各插件的适配器的工具
     */
    private final Logger log = LoggerFactory.getLogger(AdapterLoader.class);
    private final static AdapterLoader instance = new AdapterLoader();
    public final Map<String, StepAdapter<Map<String, Object>, Map<String, Object>>> cache = new ConcurrentHashMap<>();

    private AdapterLoader() {
        ServiceLoader<StepAdapter> loader = ServiceLoader.load(StepAdapter.class);
        for (StepAdapter<Map<String, Object>, Map<String, Object>> adapter : loader) {
            cache.put(adapter.type(), adapter);
        }
    }

    public static AdapterLoader getInstance() {
        return instance;
    }

    public StepAdapter<Map<String, Object>, Map<String, Object>> getStepAdapter(String type) {
        return cache.get(type);
    }
}