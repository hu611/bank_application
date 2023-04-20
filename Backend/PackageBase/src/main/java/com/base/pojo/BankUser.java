package com.base.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
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

}
