package com.omgxy.technote.system.entity.dto;

import com.omgxy.technote.system.ResultEnum;
import com.omgxy.technote.system.utils.DateUtil;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Response<T> {

    private String code;

    private String message;

    private T data;

    private Timestamp timestamp;

    public Response() {
        this.timestamp = DateUtil.now();
    }

    public Response(String code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public Response(String code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    public Response(ResultEnum resultEnum) {
        this(resultEnum.getCode(), resultEnum.getMessage());
    }

    public Response(ResultEnum resultEnum, T data) {
        this(resultEnum.getCode(), resultEnum.getMessage(), data);
    }
}
