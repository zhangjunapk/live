package org.zj.xh.bean;

import lombok.Data;

/**
 * 接口的返回数据格式
 */
@Data
public class Result<T> {
    private String status;
    private String message;
    private T data;
}
