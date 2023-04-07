package com.bank.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2023-04-06
 */
@Data
@ToString
@TableName("user_notification")
public class UserNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    private String prcId;

    private String email;

    private String cellPhone;


}
