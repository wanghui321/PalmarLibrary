package com.example.palmarlibrary;

/**
 * Created by as on 2018/5/17.
 */

public class School {
    private String schoolId;
    private String imgUrl;
    private String schoolName;
    private String province;
    public String getSchoolId(){return schoolId;}

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getImg() {
        return imgUrl;
    }

    public void setImg(String img) {
        this.imgUrl = img;
    }

    public String getName() {
        return schoolName;
    }

    public void setName(String name) {
        this.schoolName = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
