package com.fast.rpc.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能描述: RPC注解
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/18.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component // 可被Spring扫描到
public @interface RpcService {

    Class<?> value();
}
