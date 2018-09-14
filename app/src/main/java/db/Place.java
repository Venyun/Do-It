package db;

import cn.bmob.v3.BmobObject;

/**
 * Created by Venny on 2017/10/30.
 */

public class Place extends BmobObject {

    private String userAccount;
    private String studyPlace;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getStudyPlace() {
        return studyPlace;
    }

    public void setStudyPlace(String studyPlace) {
        this.studyPlace = studyPlace;
    }
}
