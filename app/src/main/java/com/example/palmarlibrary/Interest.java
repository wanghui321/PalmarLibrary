package com.example.palmarlibrary;

/**
 * Created by 52943 on 2018/5/25.
 */

public class Interest {
    private String userId;
    private Integer typeId;
    private Integer clicks;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }
}
