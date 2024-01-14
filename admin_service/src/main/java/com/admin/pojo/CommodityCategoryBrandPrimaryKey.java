package com.admin.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author gcq1109
 * @description: 类目品牌关联类主键组装
 * @email gcq1109@126.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommodityCategoryBrandPrimaryKey implements Serializable {

    private Long categoryId;

    private Long brandId;
}
