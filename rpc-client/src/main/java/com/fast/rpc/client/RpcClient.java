/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.fast.rpc.client;

import com.fast.rpc.common.codec.RpcDecoder;
import com.fast.rpc.common.codec.RpcEncoder;
import com.fast.rpc.common.entity.RpcRequest;
import com.fast.rpc.common.entity.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述: RPC 客户端 (用于发送RPC请求)
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/24.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private String host;
    private int port;

    private RpcResponse response;

    private final Object obj = new Object();

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        this.response = response;

        synchronized (obj) {
            obj.notifyAll();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client caught exception", cause);
        ctx.close();
    }

    public RpcResponse send(RpcRequest request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RpcEncoder(RpcRequest.class)) // 将 RPC 请求进行编码
                                    .addLast(new RpcDecoder(RpcResponse.class)) // 将 RPC 请求进行解码
                                    .addLast(RpcClient.this); // 使用 RpcClient 发送 Rpc 请求
                        }
                    })
            .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().writeAndFlush(request).sync();

            synchronized (obj) {
                obj.wait(); // 未收到响应, 使线程等待
            }

            if (response != null) {
                future.channel().closeFuture().sync();
            }
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }

}
