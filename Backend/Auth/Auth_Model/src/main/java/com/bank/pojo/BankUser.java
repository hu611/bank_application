package com.bank.pojo;

//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2023-04-03
 */
@Data
@ToString
//@TableName("bank_user")
public class BankUser implements Serializable {

    private static final long serialVersionUID = 1L;

    //@TableId
    private Integer id;

    private String prcId;

    private String username;

    private String realname;

    private String salt;

    private Boolean gender;

    private String cellPhone;

    private LocalDate createDate;

    private LocalDate lastUpdate;

    private LocalDate birthday;

    private String userPic;

    private String password;
}
