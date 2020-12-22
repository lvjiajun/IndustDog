package com.gree.netty.server;
import com.gree.modbus.ModbusEntity;

import javax.inject.Singleton;

/**
 * @author cici
 * @
 */
@Singleton
public class ExceptionFutureHandler implements Handler {
    private Handler nextHandler;

    public Handler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public String hander(ModbusEntity msg) {
        System.out.println("出现异常，异常信息：" + msg);
        if (nextHandler != null) {
            nextHandler.hander(msg);
        }

        return COMMONRET;
    }

}
