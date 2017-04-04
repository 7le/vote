package com.vote.dao;

import com.vote.dao.model.VoteEvent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VoteEventMapper {
    int deleteByPrimaryKey(Integer id);

    //得到自增id
    int insert(VoteEvent record);

    int insertSelective(VoteEvent record);

    VoteEvent selectByPrimaryKey(Integer id);

    List<VoteEvent> selectByTypeId(@Param("typeId") String typeId);

    VoteEvent selectByTitle(@Param("title") String title);

    List<VoteEvent> selectByInterested(@Param("interested") String[] interested);

    int updateByPrimaryKeySelective(VoteEvent record);

    int updateByPrimaryKey(VoteEvent record);
}