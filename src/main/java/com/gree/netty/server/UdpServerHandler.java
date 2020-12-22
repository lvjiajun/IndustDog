package com.gree.netty.server;
import com.gree.configure.ModbusConfigure;
import com.gree.modbus.ModbusEntity;
import com.gree.tools.ByteConverTools;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

@Singleton
public class UdpServerHandler extends
        SimpleChannelInboundHandler<DatagramPacket> {

    @Inject
    ModbusConfigure configure;

    private final Integer zeroSrc = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {

        ByteBuf byteBuf = datagramPacket.copy().content();

        final Logger logger = LoggerFactory.getLogger(UdpServerHandler.class);

        Integer byteSize = byteBuf.readableBytes();
        if ( byteSize >   0 && byteSize < 50){

            byte[] byteDate = new byte[byteSize];

            byteBuf.readBytes(byteDate);

            Integer masterId = ByteConverTools.byteArrayToInt(byteDate,configure.getMasterIndex());

            if ((byteDate[configure.getVersionIndex()] == configure.getVersionId())

                    &&(masterId.equals(configure.getMasterId()))){

                if (byteDate[configure.getCmdIndex()] == configure.getCmdActive()){

                }else if (byteDate[configure.getCmdIndex()] == configure.getCmdPassive()){

                    Integer nodeId = ByteConverTools.byteArrayToInt(byteDate,configure.getNodeIndex());

                    Integer nodeSize = ByteConverTools.byteArrayToShort(byteDate,configure.getPackSizeIndex());

                    if (nodeSize <= 1 ){

                        return;

                    }else {

                        byte[] byteModebus = new byte[nodeSize];

                        System.arraycopy(byteDate,configure.packIndex,byteModebus,zeroSrc,nodeSize);

                        ModbusEntity modbusEntity = new ModbusEntity(masterId,nodeId,nodeSize,byteModebus,true);


                    }
                }else {
                    logger.error("无效数据包{}"+ Arrays.toString(byteDate));
                    return;
                }
            }
        }else {
            byte[] byteDate = new byte[byteSize];

            byteBuf.readBytes(byteDate);

            logger.error("收到无效长度数据包数据长度{},数据{}",byteSize,Arrays.toString(byteDate));
            return;
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)
            throws Exception{
        ctx.close();
        cause.printStackTrace();
    }

}
