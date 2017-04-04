package com.vote.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录Service
 */
public interface LoginService {

    /**
     * 注册
     *
     * @param username  账号
     * @param password 登录密码
     * @param request  当前会话请求
     * @return 成功返回null, 失败返回原因
     */
    String doRegister(String username, String password, HttpServletRequest request);

    /**
     * 执行登录
     *
     * @param username  账号
     * @param password 登录密码
     * @param request  当前会话请求
     * @return 成功返回null, 失败返回原因
     */
    String doLogin(String username, String password, HttpServletRequest request);

}
