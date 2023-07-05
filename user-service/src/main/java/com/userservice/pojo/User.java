package com.userservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author gcq1109
 * @description: 用户info
 * @date 2023/7/5 21:03
 * @email gcq1109@126.com
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "passwd")
    private String passwd;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_idcard")
    private String userIdCard;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_province")
    private String userProvince;

    @Column(name = "vip_epoch")
    private Integer vipEpoch;

    @Column(name = "vip_buy_date")
    private Date vipBuyDate;

    @Column(name = "vip_end_date")
    private Date vipEndDate;

    @Column(name = "vip_status")
    private Integer vipStatus;

    @Column(name = "user_real_name")
    private String userRealName;

}
