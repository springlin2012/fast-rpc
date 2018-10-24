/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.fast.rpc.common.entity;

/**
 * 功能描述: RPC响应封装
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/22.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
public class RpcResponse {

    private String requestId;
    private Throwable error;
    private Object result;


    public boolean hasException() {
        return error != null;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
