package com.omgxy.technote.security.controller;

import com.omgxy.technote.system.ResultEnum;
import com.omgxy.technote.system.entity.dto.LoginDetail;
import com.omgxy.technote.system.entity.dto.Response;
import com.omgxy.technote.system.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/login")
public class LoginController {

    @Value("${system.security.username}")
    private String username;

    @Value("${system.security.password}")
    private String password;

    @PostMapping
    public Response<LoginDetail> login(@RequestBody LoginDetail loginDetail) {

        String username = loginDetail.getUsername();

        String password = loginDetail.getPassword();

        //1. 判断用户名密码是否为空
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("用户名或者密码为空");
        }
        if (username.equals(this.username) && password.equals(this.password)) {
            loginDetail.setToken(JwtUtil.createToken());
            return new Response<>(ResultEnum.OK, loginDetail);
        }
        throw new RuntimeException("auth failed");
    }
}
