package com.vote.dao;

import com.vote.dao.model.VoteUser;

public interface VoteUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VoteUser record);

    int insertSelective(VoteUser record);

    /**
     * 检索数量通过用户名
     *
     * @param username 用户名
     */
    int selectCountByUsername(String username);

    /**
     * 根据用户名查找管理员
     */
    VoteUser selectByUsername(String username);

    VoteUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VoteUser record);

    int updateByPrimaryKey(VoteUser record);
}