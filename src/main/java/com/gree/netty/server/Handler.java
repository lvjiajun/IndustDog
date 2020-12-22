package com.gree.netty.server;

import com.gree.modbus.ModbusEntity;

public interface Handler {
    public static String COMMONRET="200";
    public String hander(ModbusEntity msg);
}