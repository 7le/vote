package com.vote.service.impl;

import com.vote.dao.model.VoteEvent;
import com.vote.dao.model.VoteJoin;
import com.vote.dao.model.VoteOption;
import com.vote.service.VoteService;
import com.vote.util.Base64Util;
import com.vote.util.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class VoteServiceImpl extends BaseServiceImpl implements VoteService {

    public String publishVote(HttpServletRequest request,String title, String vote_describe, Integer create_id, String type_id, String option, String file,Integer endTime) {

        if (title == null || title.length() == 0) {
            return "标题不能为空";
        }

        VoteEvent baen=voteEventMapper.selectByTitle(title);
        if(baen!=null){
            return "标题重复，请换一个标题";
        }

        if (vote_describe == null || vote_describe.length() == 0) {
            return "介绍不能为空";
        }

        if (option == null) {
            return "选项不能为空";
        }

        String photoName=null;
        if(file!=null&&file.length()>0&&!("undefined").equals(file)){
            // 只允许jpg,png
            String header ="data:image/jpeg;base64,";
            String header1 ="data:iamge/png;base64,";
            System.out.println(file.toString());
            String photo[]=file.split(",");

            if(file.indexOf(header)!=0){
                photoName=TimeUtil.getTimeStamp() + ".jpg";
                Base64Util.generateImage(photo[1],"/software/tomcat1/webapps_ext/vote/"+photoName);
            }else if(file.indexOf(header1)!=0){
                photoName=TimeUtil.getTimeStamp() + ".png";
                Base64Util.generateImage(photo[1],"/software/tomcat1/webapps_ext/vote/"+photoName);
            }else{
                return "只支持jpn,png,该文件类型不允许上传";
            }
        }

        VoteEvent voteEvent = new VoteEvent();
        voteEvent.setPv(0);
        voteEvent.setCreateId(create_id);
        voteEvent.setPhotoUrl("7le.online/"+photoName);
        voteEvent.setTypeId(type_id);
        voteEvent.setVoteDescribe(vote_describe);
        voteEvent.setTitle(title);
        voteEvent.setCreateTime(TimeUtil.getTimeStamp());
        voteEvent.setEndTime(endTime);
        voteEvent.setIsEnd(0); //0表示投票中

        int eventId = voteEventMapper.insert(voteEvent);

        if (eventId > 0) {
            //切分选项
            String op[]=option.split(",");

            for (int i = 0; i <op.length ; i++) {
                for (int j = i+1; j <op.length ; j++) {
                    if(op[i].equals(op[j])){
                        return "投票选项不能相同";
                    }
                }
            }

            for(int i=0;i<op.length;i++){
                VoteOption voteOption = new VoteOption();
                voteOption.setName(op[i]);
                voteOption.setEventId(voteEvent.getId());
                voteOption.setNum(0);
                voteOptionMapper.insert(voteOption);
            }

            VoteJoin voteJoin=new VoteJoin();
            voteJoin.setEventId(voteEvent.getId());
            voteJoin.setUserId(create_id);
            voteJoin.setType(0);  // 0代表发起投票
            voteJoin.setCreateTime(TimeUtil.getTimeStamp());
            voteJoinMapper.insert(voteJoin);
        } else {
            return "发布投票失败";
        }

        return null;
    }

    public String join(Integer user_id, Integer event_id, String option) {

        if(user_id==null ||event_id==null||option==null||"undefined".equals(option)){
            return "缺少参数";
        }
        List<VoteJoin> voteJoins1=voteJoinMapper.selectByIdAndTypeAndEventId(user_id, event_id, 0);
        if(voteJoins1.size()>0){
            return "您是发起者不能投票";
        }

        List<VoteJoin> voteJoins=voteJoinMapper.selectByIdAndTypeAndEventId(user_id, event_id, 1);
        if(voteJoins.size()>0){
            return "您已经投过票了";
        }

        //参与投票
        VoteJoin voteJoin=new VoteJoin();
        voteJoin.setEventId(event_id);
        voteJoin.setUserId(user_id);
        voteJoin.setType(1);  // 1代表参加投票
        voteJoin.setCreateTime(TimeUtil.getTimeStamp());
        voteJoinMapper.insert(voteJoin);

        VoteOption voteOption= voteOptionMapper.selectByOptionAndEventId(option,event_id);

        if(voteOption==null){
            return "投票失败";
        }
        voteOption.setNum(voteOption.getNum()+1);//票数+1
        voteOptionMapper.updateByPrimaryKey(voteOption);

        return null;
    }
}
