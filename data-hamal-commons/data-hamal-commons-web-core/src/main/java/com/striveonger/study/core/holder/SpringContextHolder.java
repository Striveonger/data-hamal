package com.striveonger.study.core.holder;

import com.striveonger.study.core.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Mr.Lee
 * @description: Spring Context
 * @date 2022-11-05 15:07
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {
    private final Logger log = LoggerFactory.getLogger(SpringContextHolder.class);

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringContextHolder.context = context;
    }

    public static ApplicationContext getContext() {
        assertContext();
        return context;
    }

    public static <T> T getBean(String beanName) {
        assertContext();
        return (T) context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        assertContext();
        return context.getBean(clazz);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        assertContext();
        return context.getBean(beanName, clazz);
    }

    public static <T> T getProperties(String key, Class<T> clazz) {
        Environment env = getEnvironment();
        return env.getProperty(key, clazz);
    }

    public static <T> T getProperties(String key, T defaultValue) {
        Environment env = getEnvironment();
        return Optional.ofNullable(env.getProperty(key))
                .filter(x -> x.getClass().isInstance(defaultValue))
                .map(x -> (T) x).orElse(defaultValue);
    }

    private static Environment getEnvironment() {
        assertContext();
        return context.getEnvironment();
    }

    private static void assertContext() {
        if (SpringContextHolder.context == null) {
            // SpringContext 初始化失败～
            throw new CustomException("spring context initialization failed...");
        }
    }

}
