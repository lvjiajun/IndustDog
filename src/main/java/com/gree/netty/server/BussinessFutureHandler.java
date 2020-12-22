package com.gree.netty.server;


import com.gree.modbus.ModbusEntity;


import javax.inject.Singleton;

@SuppressWarnings("ALL")
@Singleton
public class BussinessFutureHandler implements Handler {
    private Handler nextHandler;

    public Handler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public String hander(ModbusEntity msg) {
        System.out.println("接收到信息：" + msg);
        if (nextHandler != null) {
            nextHandler.hander(msg);
        }
        return msg.toString();
    }

}