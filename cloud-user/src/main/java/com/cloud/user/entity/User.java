package com.cloud.user.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class User implements Serializable {

    private Long id;

    private String userCode;

    private String username;

    private String password;

    private Date createTime;

    private Byte status;

    private String remark;
}