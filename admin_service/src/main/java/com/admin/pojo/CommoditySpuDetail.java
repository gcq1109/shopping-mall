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
@Table(name = "commodity_spu_detail")
public class CommoditySpuDetail implements Serializable {

    @Id
    @Column(name = "spu_id")
    private Long spuId;

    @Column(name = "description")
    private Long description;

    @Column(name = "generic_attr")
    private String genericAttr;

    @Column(name = "specific_attr")
    private String specificAttr;

    @Column(name = "packing_list")
    private String packingList;

    @Column(name = "customer_service")
    private String customerService;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
