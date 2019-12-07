package com.hjs.wechat.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 实现客户端发送一个请求，服务器端会返回 hello netty
 */
public class HelloNettyService {

    public static void main(String[] args) throws Exception {

        //定义一对线程组
        //定义主线程组，用于接收客户端的请求，但是不做处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        //定义从线程组，用户处理客户端的请求
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            //netty服务器的创建 serverBOotStrap是一个启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup) //设置主从线程组
                    .channel(NioServerSocketChannel.class) // 设置nio的双向通道
                    .childHandler(new HelloNettyInitializer());                   // 子处理器，用于处理workerGroup

            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            //用户监听关闭的channel
            channelFuture.channel().closeFuture().sync();
        }finally {

            //关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }



    }

}
