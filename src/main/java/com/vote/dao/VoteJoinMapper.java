package com.vote.dao;

import com.vote.dao.model.VoteJoin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VoteJoinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VoteJoin record);

    int insertSelective(VoteJoin record);

    VoteJoin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VoteJoin record);

    int updateByPrimaryKey(VoteJoin record);

    List<VoteJoin> selectByIdAndTypeAndEventId(@Param("id") Integer id,@Param("eventId") Integer eventId,@Param("type") Integer type );
}