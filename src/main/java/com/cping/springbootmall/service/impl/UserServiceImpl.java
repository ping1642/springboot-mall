package com.cping.springbootmall.service.impl;

import com.cping.springbootmall.dao.UserDao;
import com.cping.springbootmall.dto.UserRegisterRequest;
import com.cping.springbootmall.model.User;
import com.cping.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    // log資訊
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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

        // 檢查註冊的 email
        // 先對前端的email做檢測，如果已存在就回400給前端
        // 用前端的值去查詢資料庫是否有這筆user數據存在
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if (user != null) {
            // 參數值會塞入{}內
            log.warn("該 email: {} 已經被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 創建帳號
        // 如果user不為空，就不會執行這行
        return userDao.createUser(userRegisterRequest);
    }
}
