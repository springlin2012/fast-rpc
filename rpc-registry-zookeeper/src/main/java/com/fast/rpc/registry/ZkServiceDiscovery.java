/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.fast.rpc.registry;
import com.fast.rpc.common.utils.CollectionUtil;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 功能描述: ZK实现服务发现功能
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/22.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
public class ZkServiceDiscovery implements ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(ZkServiceDiscovery.class);

    private String zkAddress;

    public ZkServiceDiscovery(String zkAddress) {
        this.zkAddress = zkAddress;
    }


    /**
     * 发现服务
     * @return
     */
    public String discover(String serviceName) {
        // 创建 ZooKeeper 客户端
        ZkClient zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
        logger.debug("connect zookeeper");
        try {
            // 获取 service 节点
            String servicePath = Constant.ZK_REGISTRY_PATH + "/" + serviceName;
            if (!zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            List<String> addressList = zkClient.getChildren(servicePath);
            if (CollectionUtil.isEmpty(addressList)) {
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            }
            // 获取 address 节点
            String address;
            int size = addressList.size();
            if (size == 1) {
                // 若只有一个地址，则获取该地址
                address = addressList.get(0);
                logger.debug("get only address node: {}", address);
            } else {
                // 若存在多个地址，则随机获取一个地址
                address = addressList.get(ThreadLocalRandom.current().nextInt(size));
                logger.debug("get random address node: {}", address);
            }
            // 获取 address 节点的值
            String addressPath = servicePath + "/" + address;
            return zkClient.readData(addressPath);
        } finally {
            zkClient.close();
        }
        
    }



}
