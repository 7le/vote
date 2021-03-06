package com.vote.controller;


import com.vote.bean.ResultBean;
import com.vote.dao.model.VoteUser;
import com.vote.util.EncryptUtil;
import com.vote.util.HttpUtil;
import com.vote.util.MD5Util;
import com.vote.util.TimeUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录控制器
 */
@Controller
@RequestMapping(value = "/user")
public class LoginController extends BaseController{

    /**
     * 注册用户
     * @param username   账号
     * @param password   密码
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "注册用户", httpMethod = "POST", response = ResultBean.class, notes = "注册用户")
    public ResultBean doRegister(HttpServletRequest request,HttpServletResponse response,
                                 @ApiParam(required = true, name = "username", value = "用户名") @RequestParam  String username,
                                 @ApiParam(required = true, name = "password", value = "密码") @RequestParam  String password) {
        String errorReport = loginService.doRegister(username,password,request);

        if (errorReport == null) {
            //进行自动登录
            //loginService.doLogin(username, password, request);
            return new ResultBean(true,null, "注册成功",null);
        } else {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,null, errorReport,null);
        }
    }

    /**
     * 账号登录
     * @param request
     * @param username 帐号
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "账号登录", httpMethod = "POST", response = ResultBean.class, notes = "账号登录")
    public ResultBean login(HttpServletRequest request,HttpServletResponse response,
                            @ApiParam(required = true, name = "username", value = "用户名") @RequestParam String username,
                            @ApiParam(required = true, name = "password", value = "密码") @RequestParam String password ) {

        String errorReport = loginService.doLogin(username,password,request);

        if (errorReport != null) {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false, null, errorReport,null);
        }
        String token=null;
        VoteUser voteUser=voteUserMapper.selectByUsername(username);
        try {
            token=EncryptUtil.aesEncrypt(voteUser.getId().toString(), EncryptUtil.KEY);
        } catch (Exception e) {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false, token, "发生错误",null);
        }
  /*      HttpSession session = request.getSession(true);
        session.setAttribute(voteUser.getId().toString(), token);
        session.setMaxInactiveInterval(60*24);*/
        voteUser.setToken(token);
        voteUser.setLeasttime(TimeUtil.getTimeStamp());
        voteUserMapper.updateByPrimaryKey(voteUser);

        voteLogger.warn("用户【" + username + "】IP【" + HttpUtil.getIpAddr(request) + "】登录了");
        return new ResultBean(true, token, "登录成功",voteUser);
    }

}
