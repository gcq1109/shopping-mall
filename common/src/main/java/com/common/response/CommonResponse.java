package com.common.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author gcq1109
 * @date 2023/7/5 19:01
 * @email gcq1109@126.com
 */
@Data
@Builder
public class CommonResponse {

    private Integer code;
    private Object content;
    private String message;
}
