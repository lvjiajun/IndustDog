package com.gree.netty.server;

import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Property;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;


@Context
public class UdpServer implements Runnable{

    @Property(name = "connect.udpServer")
    private int port ;
    private EventLoopGroup bossGroup;
    private Thread udpServerThread;

    @Inject
    UdpServerHandler udpServerHandler;

    public int getPort() {
        return port;
    }

    @PostConstruct
    public void init() {
       udpServerThread = new Thread(this);
   //    udpServerThread.start();
    }


    @PreDestroy
    public void destory() {
        System.out.println("destroy server resources");
        if (bossGroup != null){
            bossGroup.shutdownGracefully();
            bossGroup = null;
        }else {
            bossGroup = null;
        }
    }

    @Override
    public void run(){
        bossGroup=new NioEventLoopGroup();
        try
        {
            //通过NioDatagramChannel创建Channel，并设置Socket参数支持广播
            //UDP相对于TCP不需要在客户端和服务端建立实际的连接，因此不需要为连接（ChannelPipeline）设置handler
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(udpServerHandler);
            bootstrap.bind(port).sync().channel().closeFuture().await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            bossGroup.shutdownGracefully();
        }
        while (true){

            try {
                System.out.println("hello world");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
