package com.gree.netty.pipeline;
import com.gree.configure.ModbusConfigure;
import com.gree.modbus.ModbusEntity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;


public class ModbusToDecode extends ByteToMessageDecoder {

    private Integer port;

    private ModbusConfigure configure;

    public ModbusToDecode(Integer port, ModbusConfigure configure) {
        this.port = port;
        this.configure = configure;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {


        Integer byteSize = byteBuf.readableBytes();
        if (byteSize > 0){

            byte[] byteDate = new byte[byteSize];

            byteBuf.readBytes(byteDate);

            Integer index = configure.getListPort().lastIndexOf(port);

            Integer nodeId = configure.getListNode().get(index);

            ModbusEntity modbusEntity =new ModbusEntity(configure.getMasterId(),nodeId,byteSize,byteDate,false);


            list.add(modbusEntity);
        }



    }
}
