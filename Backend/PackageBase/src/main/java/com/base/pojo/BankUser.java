package com.base.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author weiyanhu
 * @since 2023-04-09
 */
@TableName("bank_user")
@ApiModel(value = "BankUser对象", description = "")
public class BankUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String prcId;

    private String username;

    private String realname;

    private String salt;

    private Boolean gender;

    private LocalDate createDate;

    private LocalDate lastUpdate;

    private LocalDate birthday;

    private String userPic;

    private String password;

    public String getPrcId() {
        return prcId;
    }

    public void setPrcId(String prcId) {
        this.prcId = prcId;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }
    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }
    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "BankUser{" +
            "prcId=" + prcId +
            ", username=" + username +
            ", realname=" + realname +
            ", salt=" + salt +
            ", gender=" + gender +
            ", createDate=" + createDate +
            ", lastUpdate=" + lastUpdate +
            ", birthday=" + birthday +
            ", userPic=" + userPic +
            ", password=" + password +
        "}";
    }
}
