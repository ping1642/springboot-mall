package com.cping.springbootmall.dao;

import com.cping.springbootmall.dto.UserRegisterRequest;
import com.cping.springbootmall.model.User;

public interface UserDao {

    // 查詢user數據
    User getUserById(Integer userId);


    // 註冊新帳號
    Integer createUser(UserRegisterRequest userRegisterRequest);
}
