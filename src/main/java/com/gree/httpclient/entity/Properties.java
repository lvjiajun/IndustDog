package com.gree.httpclient.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class Properties {

    public String VELOCITY;

    public String LOAD_DEVICE_STATE;

    public String OPERATION_STATE;

    public String mac;

    public String ALERT_STATE;

    public String WORK_STATE;

    @Override
    public String toString() {
        return "Properties{" +
                "VELOCITY='" + VELOCITY + '\'' +
                ", LOAD_DEVICE_STATE='" + LOAD_DEVICE_STATE + '\'' +
                ", OPERATION_STATE='" + OPERATION_STATE + '\'' +
                ", mac='" + mac + '\'' +
                ", ALERT_STATE='" + ALERT_STATE + '\'' +
                ", WORK_STATE='" + WORK_STATE + '\'' +
                '}';
    }

    public String getVELOCITY() {
        return VELOCITY;
    }

    public void setVELOCITY(String VELOCITY) {
        this.VELOCITY = VELOCITY;
    }

    public String getLOAD_DEVICE_STATE() {
        return LOAD_DEVICE_STATE;
    }

    public void setLOAD_DEVICE_STATE(String LOAD_DEVICE_STATE) {
        this.LOAD_DEVICE_STATE = LOAD_DEVICE_STATE;
    }

    public String getOPERATION_STATE() {
        return OPERATION_STATE;
    }

    public void setOPERATION_STATE(String OPERATION_STATE) {
        this.OPERATION_STATE = OPERATION_STATE;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getALERT_STATE() {
        return ALERT_STATE;
    }

    public void setALERT_STATE(String ALERT_STATE) {
        this.ALERT_STATE = ALERT_STATE;
    }

    public String getWORK_STATE() {
        return WORK_STATE;
    }

    public void setWORK_STATE(String WORK_STATE) {
        this.WORK_STATE = WORK_STATE;
    }
}
