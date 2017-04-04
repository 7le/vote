package com.vote.service;


import javax.servlet.http.HttpServletRequest;

public interface VoteService {

    /**
     * 发布vote
     * @return 成功返回null, 失败返回原因
     */
    String publishVote(HttpServletRequest request,String title,String vote_describe,Integer create_id,String type_id,String option,String file,Integer endTime);

    /**
     * 参加投票
     * @return成功返回null, 失败返回原因
     */
    String join(Integer user_id,Integer event_id,String option);

}
