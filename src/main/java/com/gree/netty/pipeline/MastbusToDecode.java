package com.gree.netty.pipeline;

import com.gree.configure.ModbusConfigure;
import com.gree.modbus.ModbusEntity;
import com.gree.tools.ByteConverTools;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class MastbusToDecode extends ByteToMessageDecoder {

    ModbusConfigure configure;

    private final Integer zeroSrc = 0;

    public MastbusToDecode(ModbusConfigure configure) {
        this.configure = configure;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {


        final Logger logger = LoggerFactory.getLogger(MastbusToDecode.class);

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

                        list.add(modbusEntity);
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
}
