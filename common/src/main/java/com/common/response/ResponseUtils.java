package com.common.response;

/**
 * @author gcq1109
 * @date 2023/7/5 19:04
 * @email gcq1109@126.com
 */
public class ResponseUtils {

    public static CommonResponse successResponse(Object content) {
        return CommonResponse.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .content(content)
                .build();
    }

    public static CommonResponse failResponse(Integer code, Object content, String message) {
        return CommonResponse.builder()
                .code(code)
                .message(message)
                .content(content)
                .build();
    }
}
