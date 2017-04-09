package com.vote.service.impl;

import com.vote.dao.model.VoteUser;
import com.vote.service.LoginService;
import com.vote.util.MD5Util;
import com.vote.util.TimeUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * 登录实现类
 */
@Service
public class LoginServiceImpl extends BaseServiceImpl implements LoginService {


    public String doRegister(String username, String password, HttpServletRequest request) {

        if (username == null) {
            return "请输入用户名";
        }

        if (password == null || password.length() == 0) {
            return "请输入密码";
        }

        //检查账号是否被注册
        if (voteUserMapper.selectCountByUsername(username) > 0) {
            return "该账号已经被注册";
        }

        //添加用户信息
        VoteUser voteUser = new VoteUser();
        voteUser.setUsername(username);
        voteUser.setPassword(MD5Util.doImaoMd5(username, password));
        voteUser.setCreateTime(TimeUtil.getTimeStamp());
        voteUser.setInterested("1,2"); //默认给与2个兴趣爱好
        //执行持久化
        voteUserMapper.insert(voteUser);


        return null;
    }

    public String doLogin(String username, String password, HttpServletRequest request) {

        VoteUser voteUser=voteUserMapper.selectByUsername(username);

        if (voteUser == null) {
            return "用户名不存在";
        }

        if (password == null || password.length() == 0) {
            return "请输入密码";
        }
        if (!voteUser.getPassword().equals(MD5Util.doImaoMd5(voteUser.getUsername(), password))) {
            return "账号或密码错误";
        }

        return null;
    }
}
