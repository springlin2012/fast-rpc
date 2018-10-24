/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.fast.rpc.registry;

/**
 * 功能描述: 常量
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/22.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
public interface Constant {

    int ZK_SESSION_TIMEOUT = 5000;
    int ZK_CONNECTION_TIMEOUT = 1000;

    String ZK_REGISTRY_PATH = "/registry";

    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";

}
