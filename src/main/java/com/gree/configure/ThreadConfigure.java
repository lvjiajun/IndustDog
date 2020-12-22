package com.gree.configure;

import io.micronaut.context.annotation.Property;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

@Singleton
public class ThreadConfigure {

    @Property(name = "Singthread.corePoolSize")
    public Integer corePoolSize;
    @Property(name = "Singthread.maximumPoolSize")
    public Integer maximumPoolSize;
    @Property(name = "Singthread.keepAliveTime")
    public Integer keepAliveTime;
    @Property(name = "Singthread.capacity")
    public Integer capacity;

    @PostConstruct
    public void init() {

    }

    @PreDestroy
    public void destory() {

    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public Integer getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Integer keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
