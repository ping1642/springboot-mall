package com.cping.springbootmall.controller;

import com.cping.springbootmall.dto.UserLoginRequest;
import com.cping.springbootmall.dto.UserRegisterRequest;
import com.cping.springbootmall.model.User;
import com.cping.springbootmall.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // 註冊新帳號
    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {

        // register方法會在資料庫創建一筆新的user數據，會返回資料庫生成的userId給我們
        Integer userId = userService.register(userRegisterRequest);

        // 預期userService會提供一個getUserById方法，可以根據傳進去userId的參數去資料庫中查詢這一筆user數據
        User user = userService.getUserById(userId);

        // 回傳給前端201
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // 登入功能
    // 創建新的class來實作登入功能
    @PostMapping("/users/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {

        User user = userService.login(userLoginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
