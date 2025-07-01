package com.dailylearn.proxy;

import java.lang.reflect.Proxy;

public class ProxyTest {

    public static void main(String[] args) {
        // 创建目标对象
        UserService userService = new UserServiceImpl();

        // 创建InvocationHandler
        LogHandler handler = new LogHandler(userService);

        // 创建代理对象
        UserService proxy = (UserService) Proxy.newProxyInstance(
                userService.getClass().getClassLoader(),
                userService.getClass().getInterfaces(),
                handler
        );

        // 通过代理对象调用方法
        proxy.addUser("张三");
    }
}
