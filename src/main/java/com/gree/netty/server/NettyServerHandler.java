package com.gree.netty.server;

import com.gree.configure.ModbusConfigure;
import com.gree.control.ApiController;
import com.gree.modbus.Modbus;
import com.gree.modbus.ModbusEntity;
import com.gree.modbus.ModbusMux;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.HashMap;

@Singleton
@Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<ModbusEntity>{

    final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    @Inject
    @Named("CloseFutureHandler")
    public Handler closeFutureHandler;

    @Named("ExceptionFutureHandler")
    @Inject
    public Handler exceptionFutureHandler;

    @Named("BussinessFutureHandler")
    @Inject
    public Handler bussinessFutureHandler;

    @Inject
    public ModbusConfigure configure;

    @Inject
    public ApiController apiController;

    public static HashMap<Integer, ChannelHandlerContext> chanelMap;

    public static ChannelHandlerContext channelHandlerContext;

    public NettyServerHandler() {
        chanelMap = new HashMap<>();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ModbusEntity modbusEntity) throws Exception {
        if (modbusEntity.getNodeClass()){
            if (chanelMap.containsKey(modbusEntity.nodeId)){

                ChannelHandlerContext context = chanelMap.get(modbusEntity.nodeId);
                context.writeAndFlush(modbusEntity);

            }else {
                logger.info("null point of NodeId");
            }
        }else {
            if (channelHandlerContext != null){

                channelHandlerContext.writeAndFlush(modbusEntity);
            }else {
                logger.error("master have downline");
            }
        }
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " channelRegistered " );
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " channelUnregistered " );
        super.channelUnregistered(ctx);
        if(closeFutureHandler !=null){
          //  closeFutureHandler.hander(ctx.name());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        InetSocketAddress insocket = (InetSocketAddress)ctx.channel().localAddress();
        Integer port = insocket.getPort();
        logger.info(" channelActive port {}",port);
        if (port.equals(configure.getMasterPort())){
            if(channelHandlerContext == null){

                logger.info("master Node Active Port{}",port);

            }else {

                logger.info("master Node Finish Active Port We Reconnect Port");

                channelHandlerContext.channel().close();

                logger.info("masterId Reactive Port{}",port);
            }
            channelHandlerContext = ctx;
            logger.info("masterId Active Port{}",port);
        }else {
            Integer nodeId = configure
                    .getListNode()
                    .get(configure
                            .getListPort()
                            .indexOf(port));

            logger.info("nodeId Active nodeId{} And Port{}",nodeId,port);
            if(chanelMap.containsKey(nodeId)){

                logger.info("masterId HAVE Active Port CHANLE");

                chanelMap.get(nodeId).channel().close();

                chanelMap.remove(nodeId);

                logger.info("masterId Reactive Port{}",port);
            }else{
                logger.info("masterId Active Port{}",port);
            }
            chanelMap.put(nodeId,ctx);
        }
        super.channelActive(ctx);


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress)ctx.channel().localAddress();
        Integer port = insocket.getPort();
        System.out.println(port+ " channelActive ");
        if (port.equals(configure.getMasterPort())){
            channelHandlerContext = null;
            logger.warn("master channel online");
        }else {
            Integer nodeId = configure
                    .getListNode()
                    .get(configure
                            .getListPort()
                            .indexOf(port));

            logger.warn("nodeId Active"+nodeId);

            chanelMap.remove(nodeId);
        }
        logger.warn(ctx.channel().remoteAddress() + " channelInactive " );
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn(ctx.channel().remoteAddress()+" exceptionCaught :" + cause.getMessage());
        super.exceptionCaught(ctx, cause);
        if(exceptionFutureHandler !=null){
          //  exceptionFutureHandler.hander(cause.getMessage());
        }
    }
    public static Boolean TestMastHost(Integer nodeId){
        if (channelHandlerContext == null){

            return false;
        }else {
            return true;
        }
    }
}
