/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.fast.rpc.registry;

/**
 * 功能描述: 服务注册接口
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/24.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
public interface ServiceRegistry {

    /**
     *
     * @param serviceName
     * @param serviceAddress
     */
    void register(String serviceName, String serviceAddress);
}
