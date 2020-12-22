package com.gree.netty.server;
import com.gree.configure.ModbusConfigure;

import com.gree.netty.pipeline.MastbusToDecode;
import com.gree.netty.pipeline.MastbusToEncode;
import com.gree.netty.pipeline.ModbusToEncode;
import com.gree.netty.pipeline.ModbusToDecode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {


    @Inject
    NettyServerHandler nettyServerHandler;

    @Inject
    ModbusConfigure configure;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        int localPort = socketChannel.localAddress().getPort();
        if (configure.getMasterPort() == localPort){
            pipeline.addLast("decoder", new MastbusToDecode(configure));
            pipeline.addLast("encoder", new MastbusToEncode());
        }else{
            pipeline.addLast("decoder", new ModbusToDecode(localPort,configure));
            pipeline.addLast("encoder", new ModbusToEncode(localPort,configure));
        }
        // 自己的逻辑Handler
        pipeline.addLast("handler", nettyServerHandler);
    }
}

