package db;

import android.widget.DatePicker;

import cn.bmob.v3.BmobObject;

/**
 * Created by Venny on 2017/10/26.
 */

public class Plan extends BmobObject {
    private String userAccount;
    private String planTitle;
    private String planContent;
    private String planDate;
    private String planTime;
    private Boolean ifFinished;

    public Plan(){}

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public Boolean getIfFinished() {
        return ifFinished;
    }

    public void setIfFinished(Boolean ifFinished) {
        this.ifFinished = ifFinished;
    }
}
