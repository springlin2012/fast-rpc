/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.fast.rpc.client;

import com.fast.rpc.common.entity.RpcRequest;
import com.fast.rpc.common.entity.RpcResponse;
import com.fast.rpc.registry.ZkServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * 功能描述: RPC 代理 (用于创建PRC服务代理)
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/24.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
public class RpcProxy {
    private static final Logger logger = LoggerFactory.getLogger(RpcProxy.class);

    private String serverAddress;
    private ZkServiceDiscovery serviceDiscovery;

    public RpcProxy(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public RpcProxy(ZkServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T create(final Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequest request = new RpcRequest(); // 创建并初始化 PRC 请求
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setClassName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);

                        if (serviceDiscovery != null) {
                            String serviceName = interfaceClass.getName();
                            serverAddress = serviceDiscovery.discover(serviceName); // 发现服务
                            logger.debug("discover service: {} => {}", serviceName, serverAddress);
                        }

                        String[] array = serverAddress.split(":");
                        String host = array[0];
                        int port = Integer.parseInt(array[1]);

                        RpcClient client = new RpcClient(host, port); // 初始化 RPC 客户端
                        RpcResponse response = client.send(request); // 通过 RPC 客户端发送请求

                        if (response == null) {
                            throw new RuntimeException("response is null");
                        }

                        // 返回 RPC 响应结果
                        if (response.hasException()) {
                            throw response.getError();
                        } else {
                            return response.getResult();
                        }
                    }
                }
        );
    }
}
