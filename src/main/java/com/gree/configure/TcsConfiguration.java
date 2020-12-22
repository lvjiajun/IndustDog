package com.gree.configure;

import io.micronaut.context.annotation.Property;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

@Singleton
public class TcsConfiguration {

    @Property(name = "Tcs.tcsclient")
    public String tcsclient;

    @Property(name = "Tcs.tcsport")
    public String tcsport;

    public String stopSrc;

    public String tcsSrc;

    @PostConstruct
    public void init() {
        stopSrc = tcsclient+":"+tcsport + "/v1/sendRowCommand";
        tcsSrc = tcsclient+":"+tcsport + "/v1/vehicles";
    }


    @PreDestroy
    public void destory() {

    }

    public String getTcsclient() {
        return tcsclient;
    }

    public void setTcsclient(String tcsclient) {
        this.tcsclient = tcsclient;
    }

    public String getTcsport() {
        return tcsport;
    }

    public void setTcsport(String tcsport) {
        this.tcsport = tcsport;
    }

    public String getStopSrc() {
        return stopSrc;
    }

    public void setStopSrc(String stopSrc) {
        this.stopSrc = stopSrc;
    }

    public String getTcsSrc() {
        return tcsSrc;
    }

    public void setTcsSrc(String tcsSrc) {
        this.tcsSrc = tcsSrc;
    }
}
