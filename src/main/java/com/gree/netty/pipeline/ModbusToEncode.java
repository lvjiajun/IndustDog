package com.gree.netty.pipeline;

import com.gree.configure.ModbusConfigure;
import com.gree.modbus.ModbusEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class ModbusToEncode extends MessageToByteEncoder<ModbusEntity> {

    private Integer port;

    private ModbusConfigure configure;
    public ModbusToEncode(Integer port, ModbusConfigure configure) {
        this.port = port;
        this.configure = configure;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ModbusEntity modbusEntity, ByteBuf byteBuf) throws Exception {

        if (modbusEntity.getMasterId().equals(configure.getMasterId())){
            ByteBuf buffer= Unpooled.buffer(modbusEntity.getPackSize());

            buffer.writeBytes(modbusEntity.getNodeDate());

            byteBuf.writeBytes(buffer);
        }else {

        }

    }
}
