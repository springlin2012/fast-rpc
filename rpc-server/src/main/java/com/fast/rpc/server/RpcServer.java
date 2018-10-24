/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.fast.rpc.server;

import com.fast.rpc.common.codec.RpcDecoder;
import com.fast.rpc.common.codec.RpcEncoder;
import com.fast.rpc.common.entity.RpcRequest;
import com.fast.rpc.common.entity.RpcResponse;
import com.fast.rpc.registry.ZkServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;


/**
 * 功能描述: RPC服务器
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/18.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private String serviceAddress;
    private ZkServiceRegistry serviceRegistry;

    private Map<String, Object> handlerMap = new HashMap<String, Object>(); // 存放接口名与服务对象之前的映射关系

    public RpcServer(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public RpcServer(String serviceAddress, ZkServiceRegistry serviceRegistry) {
        this.serviceAddress = serviceAddress;
        this.serviceRegistry = serviceRegistry;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取带有 @RpcService 注解的 Spring Bean
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                handlerMap.put(interfaceName, serviceBean);
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RpcDecoder(RpcRequest.class)) // 将RPC请求进行解码
                                    .addLast(new RpcEncoder(RpcResponse.class)) // 将RPC请求编码
                                    .addLast(new RpcHandler(handlerMap)); // 处理Rpc请求
                        }
                    })
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = serviceAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            ChannelFuture future = bootstrap.bind(host, port).sync();
            logger.debug("server started on port {}", port);

            if (serviceRegistry != null) {
                for (String interfaceName : handlerMap.keySet()) {
                    serviceRegistry.register(interfaceName, serviceAddress); // 注册服务地址
                    logger.debug("register service: {} => {}", interfaceName, serviceAddress);
                }
            }
            logger.debug("server started on port {}", port);

            // 关闭 RPC 服务连接
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
