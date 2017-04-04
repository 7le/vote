package com.vote.dao.model;

public class VoteJoin {
    private Integer id;

    private Integer userId;

    private Integer eventId;

    private Integer type;

    private Long createTime;

    private String title;

    private VoteEvent voteEvent;

    public VoteEvent getVoteEvent() {
        return voteEvent;
    }

    public void setVoteEvent(VoteEvent voteEvent) {
        this.voteEvent = voteEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}