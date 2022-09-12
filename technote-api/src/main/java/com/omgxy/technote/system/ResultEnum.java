package com.omgxy.technote.system;

public enum ResultEnum {

    OK("200", "OK");

    public final String code;

    public final String message;

    ResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
