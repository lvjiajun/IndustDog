package com.gree.netty.pipeline;

import com.gree.modbus.ModbusEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MastbusToEncode extends MessageToByteEncoder<ModbusEntity> {

    final Logger logger = LoggerFactory.getLogger(MastbusToEncode.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ModbusEntity modbusEntity, ByteBuf byteBuf) throws Exception {



        Integer bufferLength = 4 + 4 + modbusEntity.getNodeDate().length ;

        ByteBuf buffer= Unpooled.buffer(bufferLength);

        buffer.writeInt(modbusEntity.getMasterId());

        buffer.writeInt(modbusEntity.getNodeId());
        buffer.writeByte(bufferLength - 8);
        buffer.writeBytes(modbusEntity.getNodeDate());

        byteBuf.writeBytes(buffer);

        return;
    }
}
