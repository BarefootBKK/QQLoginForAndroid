package com.example.qqlogin.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable{
    private String headImgUrl;
    private String nickname;
    private String birthYear;
    private String gender;
    private String province;
    private String city;
    private String isYellowVIP;
    private String yellowVIPLevel;
    private String isYellowYearVIP;

    public User() {}

    public User(JSONObject jsonObject) {
        try {
            this.headImgUrl = jsonObject.getString("figureurl_qq_2");
            this.nickname = jsonObject.getString("nickname");
            this.birthYear = jsonObject.getString("year");
            this.gender = jsonObject.getString("gender");
            this.province = jsonObject.getString("province");
            this.city = jsonObject.getString("city");
            this.isYellowVIP = jsonObject.getString("is_yellow_vip");
            this.yellowVIPLevel = jsonObject.getString("yellow_vip_level");
            this.isYellowYearVIP = jsonObject.getString("is_yellow_year_vip");
        } catch (JSONException e) {
            Log.d("测试", "User: " + e.toString());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headImgUrl);
        dest.writeString(nickname);
        dest.writeString(birthYear);
        dest.writeString(gender);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(isYellowVIP);
        dest.writeString(yellowVIPLevel);
        dest.writeString(isYellowYearVIP);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            User user = new User();
            user.headImgUrl = source.readString();
            user.nickname = source.readString();
            user.birthYear = source.readString();
            user.gender = source.readString();
            user.province = source.readString();
            user.city = source.readString();
            user.isYellowVIP = source.readString();
            user.yellowVIPLevel = source.readString();
            user.isYellowYearVIP = source.readString();
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsYellowVIP() {
        if (isYellowVIP.equals("0")) {
            return "否";
        } else {
            return "是";
        }
    }

    public void setIsYellowVIP(String isYellowVIP) {
        this.isYellowVIP = isYellowVIP;
    }

    public String getYellowVIPLevel() {
        return yellowVIPLevel;
    }

    public void setYellowVIPLevel(String yellowVIPLevel) {
        this.yellowVIPLevel = yellowVIPLevel;
    }

    public String getIsYellowYearVIP() {
        if (isYellowYearVIP.equals("0")) {
            return "否";
        } else {
            return "是";
        }
    }

    public void setIsYellowYearVIP(String isYellowYearVIP) {
        this.isYellowYearVIP = isYellowYearVIP;
    }
}
