package com.javaAi.service.impl;

import com.alibaba.excel.EasyExcel;
import com.javaAi.service.UsersService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class UsersServiceImpl implements UsersService {

//    @Autowired
//    private RedissonClient redisson;
    
    @Override
    public void exportExcel(HttpServletResponse response) {
//        RLock lock = redisson.getLock("1");
    }
}
