package com.base.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author weiyanhu
 * @since 2023-04-09
 */
@Data
@ToString
@TableName("user_notification")
@ApiModel(value = "UserNotification对象", description = "")
public class UserNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    private String prcId;

    private String email;

    private String cellPhone;
}
