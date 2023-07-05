package com.common.response;

public enum ResponseCode {
    SUCCESS(200, ""),
    FAULT_REQUEST(400, ""),
    UNAUTHORIZED(401, "");

    private Integer code;
    private String message;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
