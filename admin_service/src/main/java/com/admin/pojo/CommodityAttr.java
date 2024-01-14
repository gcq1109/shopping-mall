package com.admin.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "commodity_attr")
public class CommodityAttr implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "group_id")
    private Long groupId;

    private String name;

    @Column(name = "is_numeric")
    private Integer isNumeric;

    private String unit;

    @Column(name = "is_sku_generic")
    private Integer isSkuGeneric;

    @Column(name = "is_search")
    private Integer isSearch;

    private String segments;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
