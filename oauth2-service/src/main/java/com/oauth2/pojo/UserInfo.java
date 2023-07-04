package com.oauth2.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @author gcq1109
 * @date 2023/7/4 17:24
 * @email gcq1109@126.com
 */
@Data
@Entity
@Table(name = "user")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "passwd")
    private String passwd;

    @Column(name = "user_role")
    private String userRole;

}
