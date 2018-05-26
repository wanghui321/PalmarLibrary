package com.example.palmarlibrary;

import java.util.Set;

/**
 * Created by as on 2018/5/17.
 */

public class School {
    private String schoolId;
    private String schoolName;
    private String province;
    private String imgUrl;
    private Set<User> users;
    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "School{" +
                "schoolId='" + schoolId + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", province='" + province + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", users=" + users +
                '}';
    }
}
