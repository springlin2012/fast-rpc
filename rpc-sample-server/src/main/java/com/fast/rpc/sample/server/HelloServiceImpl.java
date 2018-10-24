/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.fast.rpc.sample.server;

import com.fast.rpc.sample.api.HelloService;
import com.fast.rpc.server.RpcService;

/**
 * 功能描述: RPC实现
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/18.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello!"+ name;
    }

}
