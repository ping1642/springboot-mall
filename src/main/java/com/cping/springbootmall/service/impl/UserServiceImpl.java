package com.cping.springbootmall.service.impl;

import com.cping.springbootmall.dao.UserDao;
import com.cping.springbootmall.dto.UserRegisterRequest;
import com.cping.springbootmall.model.User;
import com.cping.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    // 查詢user數據
    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    // 註冊新帳號
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }
}
