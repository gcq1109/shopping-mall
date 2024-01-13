package com.admin.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gcq1109
 * @description: 商品类目组合
 * @date 2024/1/13 14:57
 * @email gcq1109@126.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommodityCategoryComposite {

    private Long id;

    private String name;

    private Long parentId;

    //子类目
    List<CommodityCategoryComposite> child = new ArrayList<>();
}
