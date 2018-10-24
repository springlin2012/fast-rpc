/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.fast.rpc.sample.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 功能描述: 引导服务
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/22.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
public class RpcBootstrap {
    private static final Logger logger = LoggerFactory.getLogger(RpcBootstrap.class);

    public static void main(String[] args) {
        logger.debug("start server");
        new ClassPathXmlApplicationContext("/spring/spring.xml");
    }


}
