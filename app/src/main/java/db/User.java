package db;

import java.sql.Timestamp;

import cn.bmob.v3.BmobObject;

/**
 * Created by Venny on 2017/10/21.
 */

public class User extends BmobObject {
    private String userId;
    private String userAccount;
    private String userName;
    private String passWd;
    private String image;
    private Integer friendsCount;
    private Integer groupsCount;
    private Float studyTime;

    public User(){

    }
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserName(String userName) {
        this.userName=userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }

    public String getPassWd() {
        return passWd;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setGroupsCount(int groupsCount) {
        this.groupsCount = groupsCount;
    }

    public Integer getGroupsCount() {
        return groupsCount;
    }

    public void setStudyTime(Float studyTime) {
        this.studyTime = studyTime;
    }

    public Float getStudyTime() {
        return studyTime;
    }

}
