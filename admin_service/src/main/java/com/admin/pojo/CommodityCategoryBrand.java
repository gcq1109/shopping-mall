package com.admin.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author gcq1109
 * @description: 类目品牌关联类
 * @date 2024/1/13 15:04
 * @email gcq1109@126.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(CommodityCategoryBrandPrimaryKey.class) //组装id
@Table(name = "commodity_category_brand")
public class CommodityCategoryBrand {

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
