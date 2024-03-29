package com.omgxy.technote.security.config;

import com.google.gson.Gson;
import com.omgxy.technote.system.ResultEnum;
import com.omgxy.technote.system.entity.dto.Response;
import com.omgxy.technote.system.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 设置响应编码为UTF-8
        response.setCharacterEncoding("UTF-8");
        //获取 header里的token
        String token = request.getHeader("Authorization");
        // 跨域请求options 返回为空
        String str = "OPTIONS";
        if (str.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }// 除了options跨域请求，都进入到jwt校验
        else {
            if (token == null) {
                response.getWriter().write("没有token！");
                return false;
            }
            // 校验接口传进来的token信息
            boolean verified = JwtUtil.verifyToken(token);
            if (!verified) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(new Gson().toJson(new Response<>(ResultEnum.UNAUTHORIZED)));
                return false;
            }
            return true;
        }
    }
}