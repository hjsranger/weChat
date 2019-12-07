package com.hjs.wechat.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSServiceInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline = socketChannel.pipeline();

        //添加助手类
        //websocket 基于Http协议，需要有编解码器
        pipeline.addLast(new HttpServerCodec());
        //对于大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //Httpmessage进行聚合，聚合成fullHttpRequest或FullHttpResponse
        //几乎netty中的编程都会使用到
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        //-----------------以上是对于http协议的支持------------------

        /**
         * websocket服务器处理的协议，用于指定给客户端访问的路由
         * 本handle会帮助处理一些复杂的事情，会处理握手动作，handshaking(close,ping,pone) 心跳
         * 对于websocket，都是以frames传输，不同的数据类型，定义的frames不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //添加自定义的handler
        pipeline.addLast(new ChatHandler());

    }
}
