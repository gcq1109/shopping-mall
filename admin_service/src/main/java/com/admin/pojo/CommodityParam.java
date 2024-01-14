package com.admin.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommodityParam {
    private Long brandId;

    private Long categoryId;

    private String name;

    private CommoditySpuDetail spuDetail;

    private List<CommoditySku> skus;
}
