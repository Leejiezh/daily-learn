package com.dailylearn.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogHandler implements InvocationHandler {

    private final Object target; // 被代理的对象

    public LogHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 前置通知
        System.out.println("开始执行方法: " + method.getName());

        // 执行目标方法
        Object result = method.invoke(target, args);

        // 后置通知
        System.out.println("方法执行完成: " + method.getName());

        return result;
    }
}
