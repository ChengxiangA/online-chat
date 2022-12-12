package com.chengxiang.chat.dto;

import com.chengxiang.chat.annotition.EnumValue;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * @author 程祥
 * @date 2022/11/28 10:20
 */
@Data
public class UserInfoDTO {
    private String id;

    @Length(min = 6, max = 20,message = "密码最少6位，最长20位")
    private String password;

    @EnumValue(intValues = {0, 1}, message = "性别只能是0和1")
    private Integer sex;

    @Length(min = 11, max = 11,message = "手机号码只能为11位")
    private String phone;
}
