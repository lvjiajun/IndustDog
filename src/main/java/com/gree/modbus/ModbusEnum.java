package com.gree.modbus;

public enum ModbusEnum {

    DOOR_CLOSE(64),
    DOOR_CLSE_STOP(96),
    DOOR_OPEN(-128),
    DOOR_OPEN_STOP(-96),
    DOOR_AUTO_OPEN(-120),
    DOOR_AUTO_OPEN_STOP(-88),
    DOOR_AUTO_CLOSE(68),
    DOOR_AUTO_CLOSE_STOP(100),
    DOOR_AUTO_OPEN_STOP_SET(40),
    DOOR_AUTO_CLOSE_STOP_SET(36),
    STOP(32),
    ERR_OUTTIME(0x09),
    NULL(0x00);

    private final int info;

    ModbusEnum(int info) {

        this.info = info;
    }

    public int getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "ModbusEnum{" +
                "info=" + info +
                '}';
    }
}
