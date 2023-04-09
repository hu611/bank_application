package com.storage.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("user_notification")
@ApiModel(value = "UserNotification对象", description = "")
public class UserNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    private String prcId;

    private String email;

    private String cellPhone;

    public String getPrcId() {
        return prcId;
    }

    public void setPrcId(String prcId) {
        this.prcId = prcId;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    @Override
    public String toString() {
        return "UserNotification{" +
            "prcId=" + prcId +
            ", email=" + email +
            ", cellPhone=" + cellPhone +
        "}";
    }
}
