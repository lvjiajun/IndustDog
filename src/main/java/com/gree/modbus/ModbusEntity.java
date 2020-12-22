package com.gree.modbus;

import java.util.Arrays;

public class ModbusEntity {

    public Integer masterId;

    public Integer nodeId;

    private Integer packSize;

    private byte[] nodeDate;

    private Boolean nodeClass;
    public ModbusEntity(Integer masterId, Integer nodeId, Integer packSize, byte[] nodeDate,Boolean nodeClass) {
        this.masterId = masterId;
        this.nodeId = nodeId;
        this.packSize = packSize;
        this.nodeDate = nodeDate;
        this.nodeClass = nodeClass;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getPackSize() {
        return packSize;
    }

    public void setPackSize(Integer packSize) {
        this.packSize = packSize;
    }

    public byte[] getNodeDate() {
        return nodeDate;
    }

    public void setNodeDate(byte[] nodeDate) {
        this.nodeDate = nodeDate;
    }

    public Boolean getNodeClass() {
        return nodeClass;
    }

    public void setNodeClass(Boolean nodeClass) {
        this.nodeClass = nodeClass;
    }

    @Override
    public String toString() {
        return "ModbusEntity{" +
                "masterId=" + masterId +
                ", nodeId=" + nodeId +
                ", packSize=" + packSize +
                ", nodeDate=" + Arrays.toString(nodeDate) +
                ", nodeClass=" + nodeClass +
                '}';
    }
}
