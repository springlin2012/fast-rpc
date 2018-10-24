package com.fast.rpc.registry;

/**
 * 功能描述: 服务发现接口
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/24.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
public interface ServiceDiscovery {

    /**
     * 根据服务名称查找服务地址
     *
     * @param serviceName
     * @return
     */
    String discover(String serviceName);
}
