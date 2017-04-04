package com.vote.controller;

import com.vote.dao.VoteEventMapper;
import com.vote.dao.VoteJoinMapper;
import com.vote.dao.VoteOptionMapper;
import com.vote.dao.VoteUserMapper;
import com.vote.service.VoteService;
import org.apache.log4j.Logger;
import com.vote.service.LoginService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 总控制器
 */
public class BaseController {

    protected static final Logger voteLogger = Logger.getLogger("vote");


    /**
     * 业务层
     */
    @Autowired
    protected LoginService loginService;
    @Autowired
    protected VoteService voteService;

    //dao
    @Autowired
    protected VoteEventMapper voteEventMapper;
    @Autowired
    protected VoteUserMapper voteUserMapper;
    @Autowired
    protected VoteJoinMapper voteJoinMapper;
    @Autowired
    protected VoteOptionMapper voteOptionMapper;

    /** jq Grid **/
    /**
     * 请求要显示的页数 *
     */
    protected int page = 1;
    /**
     * 显示的数据条数 *
     */
    protected int rows = 10;
    /**
     * 排序依据
     */
    protected String sidx = "";
    /**
     * 排序顺序
     */
    protected String sord = "";


    /**
     * 记录管理员操作
     *
     * @return
     */
/*    protected int recordAdminOperation(String operation, Object admin) {
        AdminOperationRecord aor = new AdminOperationRecord();
        aor.setAdminId(admin.getAdminId());
        aor.setRecordDescribe(operation);
        aor.setRecordTime(System.currentTimeMillis());
        return adminOperationRecordMapper.insert(aor);
    }*/


    /**
     * 执行输出流响应
     *
     * @param response 当前http会话响应对象
     * @param mimeType 内容类型
     * @param data     字节数据
     */
    protected void doByteResponse(HttpServletResponse response, String mimeType, byte[] data) throws IOException {

        // 设置响应的类型格式为JSON格式
        response.setContentType(mimeType);
        //禁止缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        //数据输出
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(data);
        outputStream.flush();
    }


    /**
     * 执行json响应
     *
     * @param response 当前http会话响应对象
     * @param result   结果对象
     */
    protected void doJsonResponse(HttpServletResponse response, Object result) throws IOException {

        // 设置响应的类型格式为JSON格式
        response.setContentType("application/json;charset=UTF-8");
        //禁止缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        response.getWriter().append(JSONObject.fromObject(result).toString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
