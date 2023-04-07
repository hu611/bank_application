package com.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegisterDto {
    String prcId;
    String username;
    String realname;
    String gender;
    String cellPhone;
    String password;
    String Email;
    String salt;
}
