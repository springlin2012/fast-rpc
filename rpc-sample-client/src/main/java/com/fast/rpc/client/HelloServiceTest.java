/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.fast.rpc.client;

import com.fast.rpc.sample.api.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 功能描述: 单元测试
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/24.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
public class HelloServiceTest {


    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/spring/spring.xml");
        RpcProxy rpcProxy = context.getBean(RpcProxy.class);

        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello("World");
        System.out.println(result);

    }
}
