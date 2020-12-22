package com.gree.configure;

import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Property;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Singleton

public class ModbusConfigure {

    @Property(name = "proto.versionIndex")
    public Integer versionIndex;
    @Property(name = "proto.versionId")
    public Integer versionId;
    @Property(name = "proto.cmdIndex")
    public Integer cmdIndex;
    @Property(name = "proto.cmdActive")
    public Integer cmdActive;
    @Property(name = "proto.cmdPassive")
    public Integer cmdPassive;
    @Property(name = "proto.masterIndex")
    public Integer masterIndex;
    @Property(name = "proto.masterId")
    public Integer masterId;
    @Property(name = "proto.nodeIndex")
    public Integer nodeIndex;
    @Property(name = "proto.nodeList")
    private List<Integer> listNode;
    @Property(name = "proto.nodePort")
    private List<Integer> listPort;
    @Property(name = "proto.packSizeIndex")
    public Integer packSizeIndex;
    @Property(name = "proto.packIndex")
    public Integer packIndex;
    @Property(name = "connect.tcpServer")
    private Integer masterPort;
    @Property(name = "proto.point")
    private List<List<String>> Point;


    @PostConstruct
    public void init() {

    }


    @PreDestroy
    public void destory() {

    }

    public Integer getVersionIndex() {
        return versionIndex;
    }

    public void setVersionIndex(Integer versionIndex) {
        this.versionIndex = versionIndex;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public Integer getCmdIndex() {
        return cmdIndex;
    }

    public void setCmdIndex(Integer cmdIndex) {
        this.cmdIndex = cmdIndex;
    }

    public Integer getCmdActive() {
        return cmdActive;
    }

    public void setCmdActive(Integer cmdActive) {
        this.cmdActive = cmdActive;
    }

    public Integer getCmdPassive() {
        return cmdPassive;
    }

    public void setCmdPassive(Integer cmdPassive) {
        this.cmdPassive = cmdPassive;
    }

    public Integer getMasterIndex() {
        return masterIndex;
    }

    public void setMasterIndex(Integer masterIndex) {
        this.masterIndex = masterIndex;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public Integer getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(Integer nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public List<Integer> getListNode() {
        return listNode;
    }

    public void setListNode(List<Integer> listNode) {
        this.listNode = listNode;
    }

    public List<Integer> getListPort() {
        return listPort;
    }

    public void setListPort(List<Integer> listPort) {
        this.listPort = listPort;
    }

    public Integer getPackSizeIndex() {
        return packSizeIndex;
    }

    public void setPackSizeIndex(Integer packSizeIndex) {
        this.packSizeIndex = packSizeIndex;
    }

    public Integer getPackIndex() {
        return packIndex;
    }

    public void setPackIndex(Integer packIndex) {
        this.packIndex = packIndex;
    }

    public Integer getMasterPort() {
        return masterPort;
    }

    public void setMasterPort(Integer masterPort) {
        this.masterPort = masterPort;
    }

    public List<List<String>> getPoint() {
        return Point;
    }

    public void setPoint(List<List<String>> point) {
        Point = point;
    }
}
