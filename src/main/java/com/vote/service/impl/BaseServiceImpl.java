package com.vote.service.impl;


import com.vote.dao.VoteEventMapper;
import com.vote.dao.VoteJoinMapper;
import com.vote.dao.VoteOptionMapper;
import com.vote.dao.VoteUserMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseServiceImpl {


    protected static final Logger voteLogger = Logger.getLogger("vote");

    /**
     * mybatis dao接口
     */
    @Autowired
    protected VoteEventMapper voteEventMapper;
    @Autowired
    protected VoteJoinMapper voteJoinMapper;
    @Autowired
    protected VoteOptionMapper voteOptionMapper;
    @Autowired
    protected VoteUserMapper voteUserMapper;

}
