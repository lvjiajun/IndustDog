package com.gree.httpclient.entity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.micronaut.core.annotation.Introspected;



@Introspected
@JsonDeserialize(as = TcsEntity.class)
public class TcsEntity {

    public String name;

    public Properties properties;

    public Integer length;

    public Integer energyLevelGood;

    public Integer energyLevelCritical;

    public Integer energyLevel;

    public String integrationLevel;

    public String procState;

    public String transportOrder;

    public String currentPosition;

    public String nextPosition;

    public String state;

    @Override
    public String toString() {
        return "TcsEntity{" +
                "name='" + name + '\'' +
                ", properties=" + properties +
                ", length=" + length +
                ", energyLevelGood=" + energyLevelGood +
                ", energyLevelCritical=" + energyLevelCritical +
                ", energyLevel=" + energyLevel +
                ", integrationLevel='" + integrationLevel + '\'' +
                ", procState='" + procState + '\'' +
                ", transportOrder='" + transportOrder + '\'' +
                ", currentPosition='" + currentPosition + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getEnergyLevelGood() {
        return energyLevelGood;
    }

    public void setEnergyLevelGood(Integer energyLevelGood) {
        this.energyLevelGood = energyLevelGood;
    }

    public Integer getEnergyLevelCritical() {
        return energyLevelCritical;
    }

    public void setEnergyLevelCritical(Integer energyLevelCritical) {
        this.energyLevelCritical = energyLevelCritical;
    }

    public Integer getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(Integer energyLevel) {
        this.energyLevel = energyLevel;
    }

    public String getIntegrationLevel() {
        return integrationLevel;
    }

    public void setIntegrationLevel(String integrationLevel) {
        this.integrationLevel = integrationLevel;
    }

    public String getProcState() {
        return procState;
    }

    public void setProcState(String procState) {
        this.procState = procState;
    }

    public String getTransportOrder() {
        return transportOrder;
    }

    public void setTransportOrder(String transportOrder) {
        this.transportOrder = transportOrder;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(String nextPosition) {
        this.nextPosition = nextPosition;
    }
}
