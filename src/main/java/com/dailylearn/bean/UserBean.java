package com.dailylearn.bean;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class UserBean implements BeanNameAware, InitializingBean {
    private String name;
    @Override
    public void setBeanName(String s) {
        s = "leeBean";
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("-------初始化前置处理-----");
        name = "leejie";
    }
} 