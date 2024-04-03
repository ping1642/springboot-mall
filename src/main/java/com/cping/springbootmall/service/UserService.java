package com.cping.springbootmall.service;

import com.cping.springbootmall.dto.UserRegisterRequest;
import com.cping.springbootmall.model.User;

public interface UserService {

    // 查詢user數據
    User getUserById(Integer userId);

    // 註冊新帳號
    Integer register(UserRegisterRequest userRegisterRequest);
}
