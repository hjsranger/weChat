package com.hjs.wechat.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * 处理消息的Handler
 * 在netty中，用于为ws专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //用于记录和管理所有客户端的channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {

        //获取客户端传输的消息
        String content = msg.text();
        System.out.println("接收到的数据："+content);

        for (Channel client : clients) {
            String returnMsg = "[服务器接收到消息：]"+LocalDateTime.now()+"接收到消息："+content;
            client.writeAndFlush(new TextWebSocketFrame(returnMsg));
        }

        //和上面的for循环是一样的
        //clients.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息：]"+LocalDateTime.now()+"接收到消息：")+content);

    }


    /**
     * 当客户端打开链接后
     * 获取客户端的channel，并添加到group中
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //会自动触发移出
        //clients.remove(ctx.channel());
        System.out.println("客户端断开连接："+ctx.channel().id().asShortText());
        System.out.println("客户端断开连接："+ctx.channel().id().asLongText());
    }

}
