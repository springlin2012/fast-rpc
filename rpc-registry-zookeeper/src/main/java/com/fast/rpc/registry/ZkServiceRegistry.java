/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.fast.rpc.registry;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


/**
 * 功能描述: 服务注册
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/18.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
public class ZkServiceRegistry implements ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ZkServiceRegistry.class);

    private final ZkClient zkClient;

    public ZkServiceRegistry(String zkAddress) {
        // 创建 ZK 客户端连接
        zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        // 创建 registry 节点(持久)
        String registryPath = Constant.ZK_REGISTRY_PATH;
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            logger.debug("create registry node: {}", registryPath);
        }

        // 创建 service 节点 (持久)
        String servicePath = registryPath.concat("/").concat(serviceName);
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            logger.debug("create server node: {}", servicePath);
        }

        // 创建 address 节点 (临时)
        String addressPath = servicePath.concat("/address-");
        String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);
        logger.debug("create address node: {}", addressNode);
    }
}
