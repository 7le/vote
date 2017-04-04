package com.vote.dao.model;

import java.util.List;

public class VoteEvent {
    private Integer id;

    private String title;

    private String photoUrl;

    private String voteDescribe;

    private Integer createId;

    private Integer pv;

    private String typeId;

    private Long createTime;

    private Integer endTime;

    private Integer isEnd;

    private List<VoteOption> voteOptionList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl == null ? null : photoUrl.trim();
    }

    public String getVoteDescribe() {
        return voteDescribe;
    }

    public void setVoteDescribe(String voteDescribe) {
        this.voteDescribe = voteDescribe == null ? null : voteDescribe.trim();
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId == null ? null : typeId.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Integer isEnd) {
        this.isEnd = isEnd;
    }

    public List<VoteOption> getVoteOptionList() {
        return voteOptionList;
    }

    public void setVoteOptionList(List<VoteOption> voteOptionList) {
        this.voteOptionList = voteOptionList;
    }
}