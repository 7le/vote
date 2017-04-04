package com.vote.dao;

import com.vote.dao.model.VoteOption;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VoteOptionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VoteOption record);

    int insertSelective(VoteOption record);

    VoteOption selectByPrimaryKey(Integer id);

    List<VoteOption> selectByEventId(@Param("eventId")Integer eventId);

    VoteOption selectByOptionAndEventId(@Param("option")String option,@Param("eventId")Integer eventId);

    int updateByPrimaryKeySelective(VoteOption record);

    int updateByPrimaryKey(VoteOption record);
}