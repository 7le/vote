package com.vote.controller;

import com.vote.bean.ResultBean;
import com.vote.dao.model.VoteEvent;
import com.vote.dao.model.VoteJoin;
import com.vote.dao.model.VoteUser;
import com.vote.util.EncryptUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 投票控制器
 */
@Controller
@RequestMapping(value = "/vote")
public class VoteController extends BaseController {

    /**
     * 投票发布
     */
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "投票发布", httpMethod = "POST", response = ResultBean.class, notes = "投票发布")
    public ResultBean publish(HttpServletRequest request,HttpServletResponse response,
                              @ApiParam(required = true, name = "title", value = "标题") @RequestParam String title,
                              @ApiParam(required = true, name = "vote_describe", value = "描述") @RequestParam String vote_describe,
                              @ApiParam(required = true, name = "type_id", value = "分类id") @RequestParam String type_id,
                              @ApiParam(required = true, name = "option", value = "选项，多个选项拼接用,隔开") @RequestParam String option,
                              @ApiParam(required = true, name = "endTime", value = "结束时间,1为1天，2位7天，3位30天") @RequestParam Integer endTime,
                              @ApiParam(required = false, name = "file", value = "图片") @RequestParam String file) {
        Integer user_id=null;
        try {
            user_id=Integer.valueOf(EncryptUtil.aesDecrypt((String)request.getAttribute("token"), EncryptUtil.KEY));
        } catch (Exception e) {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,(String)request.getAttribute("token"), "token无效", null);
        }
        String errorReport = voteService.publishVote(request,title, vote_describe, user_id, type_id, option, file,endTime);

        if (errorReport != null) {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,(String)request.getAttribute("token"), errorReport, null);
        } else {
            return new ResultBean(true,null, "发布成功", null);
        }
    }

    /**
     * 投票事件展示
     */
    @RequestMapping(value = "/show/{event_id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "投票事件展示", httpMethod = "GET", response = ResultBean.class, notes = "投票事件展示")
    public ResultBean show(HttpServletRequest request,HttpServletResponse response,
                           @ApiParam(required = true, name = "event_id", value = "投票事件id") @PathVariable Integer event_id) {

        //要判断是否过期 需要增加截至时间 是否结束  增加定时任务12点去扫描表

        VoteEvent bean = voteEventMapper.selectByPrimaryKey(event_id);

        bean.setPv(bean.getPv() + 1);

        voteEventMapper.updateByPrimaryKey(bean);

        if (bean != null) {
            return new ResultBean(true, (String)request.getAttribute("token"),"展示成功", bean);
        } else {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false, null,"展示失败", null);
        }
    }

    /**
     * 参与投票
     */
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "参与投票", httpMethod = "POST", response = ResultBean.class, notes = "参与投票")
    public ResultBean join(HttpServletRequest request,HttpServletResponse response,
                           @ApiParam(required = true, name = "event_id", value = "投票事件id") @RequestParam Integer event_id,
                           @ApiParam(required = true, name = "option", value = "选项") @RequestParam String option) {
        Integer user_id=null;
        try {
            user_id=Integer.valueOf(EncryptUtil.aesDecrypt((String)request.getAttribute("token"), EncryptUtil.KEY));
        } catch (Exception e) {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,(String)request.getAttribute("token"), "token无效", null);
        }
        if(voteEventMapper.selectByPrimaryKey(event_id).getEndTime()==1){
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,(String)request.getAttribute("token"), "投票已经结束", null);
        }

        String errorReport = voteService.join(user_id, event_id, option);

        if (errorReport != null) {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,(String)request.getAttribute("token"), errorReport, null);
        } else {
            return new ResultBean(true,null, "参与成功", null);
        }
    }

    /**
     * 热门展示和分类list展示
     */
    @RequestMapping(value = "/list/{sort_id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "热门展示和分类展示", httpMethod = "GET", response = ResultBean.class, notes = "热门展示和分类展示")
    public ResultBean list(HttpServletRequest request,HttpServletResponse response,
            @ApiParam(required = true, name = "sort_id", value = "分类ID，0时 为搜索热门,1~6时为分类展示") @PathVariable String sort_id) {

        //当sort_id=0时 为搜索热门,当sort_id=1~6时为分类展示
        List<VoteEvent> voteEvents=voteEventMapper.selectByTypeId(sort_id);

        if (voteEvents != null) {
            return new ResultBean(true,(String)request.getAttribute("token"),"展示成功", voteEvents);
        } else {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,null, "展示失败", null);
        }
    }

    /**
     * 感兴趣list展示
     */
    @RequestMapping(value = "/interested/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "感兴趣list展示", httpMethod = "GET", response = ResultBean.class, notes = "感兴趣list展示")
    public ResultBean interestedList(HttpServletRequest request,HttpServletResponse response) {
        Integer user_id=null;
        try {
            user_id=Integer.valueOf(EncryptUtil.aesDecrypt((String)request.getAttribute("token"), EncryptUtil.KEY));
        } catch (Exception e) {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,(String)request.getAttribute("token"), "token无效", null);
        }

        VoteUser voteUser= voteUserMapper.selectByPrimaryKey(user_id);

        if (voteUser.getInterested() != null) {
            String []interested=voteUser.getInterested().split(",");
            List<VoteEvent> voteEvents=voteEventMapper.selectByInterested(interested);
            return new ResultBean(true,(String)request.getAttribute("token"), "展示成功", voteEvents);
        } else {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,null, "用户没有设置感兴趣标签", null);
        }
    }

    /**
     * 添加兴趣
     */
    @RequestMapping(value = "/interested/add", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "添加兴趣", httpMethod = "POST", response = ResultBean.class, notes = "添加兴趣")
    public ResultBean interested(HttpServletRequest request,HttpServletResponse response,
                                  @ApiParam(required = true, name = "interested", value = "感兴趣，多选的时候类似1，2，3") @RequestParam String interested) {
        Integer user_id=null;
        try {
            user_id=Integer.valueOf(EncryptUtil.aesDecrypt((String)request.getAttribute("token"), EncryptUtil.KEY));
        } catch (Exception e) {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,(String)request.getAttribute("token"), "token无效", null);
        }

        VoteUser voteUser= voteUserMapper.selectByPrimaryKey(user_id);
        voteUser.setInterested(interested);
        int flag=voteUserMapper.updateByPrimaryKey(voteUser);
        if (flag == 1) {
            return new ResultBean(true,(String)request.getAttribute("token"), "添加成功", null);
        } else {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,null, "添加失败", null);
        }
    }

    /**
     * 参加的投票
     */
    @RequestMapping(value = "/list/join", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "参加的投票", httpMethod = "GET", response = ResultBean.class, notes = "参加的投票")
    public ResultBean listJoin( HttpServletRequest request,HttpServletResponse response) {
        Integer user_id=null;
        try {
            user_id=Integer.valueOf(EncryptUtil.aesDecrypt((String)request.getAttribute("token"), EncryptUtil.KEY));
        } catch (Exception e) {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,(String)request.getAttribute("token"), "token无效", null);
        }

        List<VoteJoin> voteJoins = voteJoinMapper.selectByIdAndTypeAndEventId(user_id, null, 1);// 1代表参加投票
        if (voteJoins != null) {
            return new ResultBean(true,(String)request.getAttribute("token"), "展示成功", voteJoins);
        } else {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,null, "展示失败", null);
        }
    }

    /**
     * 发起的投票
     */
    @RequestMapping(value = "/list/publish", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "发起的投票", httpMethod = "GET", response = ResultBean.class, notes = "发起的投票")
    public ResultBean listPublish(HttpServletRequest request,HttpServletResponse response) {
        Integer user_id=null;
        try {
            user_id=Integer.valueOf(EncryptUtil.aesDecrypt((String)request.getAttribute("token"), EncryptUtil.KEY));
        } catch (Exception e) {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,(String)request.getAttribute("token"), "token无效", null);
        }

        List<VoteJoin> voteJoins = voteJoinMapper.selectByIdAndTypeAndEventId(user_id, null, 0); // 0代表发起投票
        if (voteJoins != null) {
            return new ResultBean(true,(String)request.getAttribute("token"), "展示成功", voteJoins);
        } else {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,null, "展示失败", null);
        }
    }

    /**
     * 用户资料
     */
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "用户资料", httpMethod = "GET", response = ResultBean.class, notes = "用户资料")
    public ResultBean userInfo(HttpServletRequest request,HttpServletResponse response) {
        Integer user_id=null;
        try {
            user_id=Integer.valueOf(EncryptUtil.aesDecrypt((String)request.getAttribute("token"), EncryptUtil.KEY));
        } catch (Exception e) {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,(String)request.getAttribute("token"), "token无效", null);
        }

        VoteUser voteUser=voteUserMapper.selectByPrimaryKey(user_id);
        if (voteUser != null) {
            return new ResultBean(true,(String)request.getAttribute("token"), "用户资料展示成功", voteUser);
        } else {
            response.setStatus(response.SC_FORBIDDEN);
            return new ResultBean(false,null, "用户资料展示失败", null);
        }
    }
}
