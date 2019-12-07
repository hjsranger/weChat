package com.hjs.wechat.service;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 初始化，channel注册后，会执行里面相应的初始化方法
 */
public class HelloNettyInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        //通过socketChannel获取对应的管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        //通过管道添加Handler

        //当请求到达服务器端，我们需要解码，相应到客户端，需要编码
        pipeline.addLast("HttpServiceCodec",new HttpServerCodec());
        pipeline.addLast("customHandler",new CustomHandler());

    }
}
