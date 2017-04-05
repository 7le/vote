package com.vote.interceptor;

import com.vote.bean.ResultBean;
import com.vote.dao.VoteUserMapper;
import com.vote.dao.model.VoteUser;
import com.vote.util.EncryptUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录拦截器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private VoteUserMapper voteUserMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //HttpSession session = request.getSession();
        //String token =request.getParameter("token");
        String token =request.getHeader("Authorization");
        //String token="ERR15zAv0B1PA64RfD9BTA==";
        if(token!=null){
            String id= EncryptUtil.aesDecrypt(token,EncryptUtil.KEY);

            VoteUser voteUser=voteUserMapper.selectByPrimaryKey(Integer.valueOf(id));

            //String token1 = (String) session.getAttribute(id);
            String token1 = voteUser.getToken();
            if(token.equals(token1)){
                request.setAttribute("token",token);
                return true;
            }
        }

        ResultBean resultBean=new ResultBean(false,"token无效",null,null);
        JSONObject responseJSONObject = JSONObject.fromObject(resultBean);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(responseJSONObject.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }

        return false;
    }


}
