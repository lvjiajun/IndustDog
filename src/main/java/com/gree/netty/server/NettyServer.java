package com.gree.netty.server;

import com.gree.configure.ModbusConfigure;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Property;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.ArrayList;
/**
 * @author cici
 */
@Context
public class NettyServer implements Runnable {

    @Property(name = "connect.tcpServer")
    private Integer port ;

    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Thread nserver;
    private ArrayList<ChannelFuture> channelFutures;

    @Inject
    ModbusConfigure configure;

    @Inject
    NettyServerInitializer nettyServerInitializer;

    final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @PostConstruct
    public void init() {
        channelFutures = new ArrayList<>();
        nserver = new Thread(this);
        nserver.start();
        System.out.println("netty is start");
    }

    @PreDestroy
    public void destory() {
        System.out.println("destroy server resources");
        if (null == channel) {
            System.out.println("server channel is null");
        }
        if ((bossGroup !=null)&&(workerGroup !=null)&&(channel!=null)){
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            channel.closeFuture().syncUninterruptibly();
        }
        bossGroup = null;
        workerGroup = null;
        channel = null;
    }

    @Override
    public void run() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup);

            b.channel(NioServerSocketChannel.class);

            b.childHandler(nettyServerInitializer);

            // 服务器绑定端口监听
            ChannelFuture f = b.bind(port).sync();

            for (Integer port : configure.getListPort()){

                channelFutures.add(b.bind(port).sync());
                // 监听服务器关闭监听
                logger.info("开启端口成功 {}",port);
            }
            f.channel().closeFuture().sync();

            channel = f.channel();

        } catch (InterruptedException e) {
            logger.error("客户端端口出现占用 {} Master {} port list {}",e.getMessage(),port,configure.getListPort());

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}