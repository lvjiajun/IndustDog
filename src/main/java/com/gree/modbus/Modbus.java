package com.gree.modbus;
import com.gree.configure.PriorityThreadPoolExecutor;

import com.gree.netty.server.NettyServerHandler;
import com.gree.tools.ByteConverTools;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;

import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Future;



public class Modbus {

    public ModbusEnum modbusEnum;

    public Future<ModbusEnum> modbusEnumFuture;

    ModbusFactory modbusFactory;

    ModbusMaster modbusMaster;

    PriorityThreadPoolExecutor threadPoolExecutor;

    Integer nodeId;

    List<String> nodePoint;

    final Logger logger = LoggerFactory.getLogger(Modbus.class);

    public Modbus(Integer port,
                  Integer nodeId,
                  PriorityThreadPoolExecutor threadPoolExecutor,
                  List<String> nodePoint) throws ModbusInitException {

        this.nodeId = nodeId;

        this.threadPoolExecutor = threadPoolExecutor;

        this.nodePoint = nodePoint;

        modbusFactory = new ModbusFactory();

        IpParameters params = new IpParameters();

        params.setHost("localhost");

        params.setPort(port);

        params.setEncapsulated(true);

        modbusMaster = modbusFactory.createTcpMaster(params, true);

        modbusMaster.setTimeout(500);

        modbusMaster.setRetries(3);

        modbusMaster.init();

        modbusMaster.setConnected(true);
    }
    public Future<Boolean> coil(int index){return threadPoolExecutor.submit(()->fastReadCoil(index));}
    public Future<Boolean> state(int index){ return threadPoolExecutor.submit(()->fastReadState(index));}
    public Future<Boolean> open(){
        return threadPoolExecutor.submit(this::openDoorStata);
    }
    public Future<Boolean> close(){
        return threadPoolExecutor.submit(this::shutDoorState);
    }
    public Future<Boolean> reset(){
        return threadPoolExecutor.submit(this::resetDoorState);
    }
    public Future<ModbusEnum> read(){return threadPoolExecutor.submit(this::readDoorStata);}

    public Future<Boolean> coil(int index,int priority){return threadPoolExecutor.submit(()->fastReadCoil(index),priority);}
    public Future<Boolean> state(int index,int priority){return threadPoolExecutor.submit(()->fastReadState(index),priority);}
    public Future<Boolean> open(int priority){
        return threadPoolExecutor.submit(this::openDoorStata,priority);
    }
    public Future<Boolean> close(int priority){
        return threadPoolExecutor.submit(this::shutDoorState,priority);
    }
    public Future<Boolean> reset(int priority){
        return threadPoolExecutor.submit(this::resetDoorState,priority);
    }
    public Future<ModbusEnum> read(int priority){return threadPoolExecutor.submit(this::readDoorStata,priority);}


    public Boolean fastReadCoil(Integer Index){
        if (NettyServerHandler.TestMastHost(nodeId)){
            try {
                boolean[] bdata_b = ModbusUtit.readCoilStatus(0x11,0x00,modbusMaster,0x04);
                return bdata_b[Index];
            } catch (ModbusTransportException e) {
                logger.warn(e.getMessage());
                return false;
            }
        }else {
            return false;
        }
    }

    public Boolean fastReadState(Integer Index){
        if (NettyServerHandler.TestMastHost(nodeId)){
            try {
                boolean[] bdata_a = ModbusUtit.readInputStatus(0x11,0x20,modbusMaster,0x04);
                return bdata_a[Index];
            } catch (ModbusTransportException e) {
                logger.warn(e.getMessage());
                return false;
            }
        }else {
            return false;
        }
    }
    public ModbusEnum readDoorStata(){
        if (NettyServerHandler.TestMastHost(nodeId)){
            try {
                boolean[] bdata_a = ModbusUtit.readInputStatus(0x11,0x20,modbusMaster,0x04);
                boolean[] bdata_b = ModbusUtit.readCoilStatus(0x11,0x00,modbusMaster,0x04);
                boolean[] data = new boolean[8];
                System.arraycopy(bdata_a, 0, data, 0, 4);
                System.arraycopy(bdata_b, 0, data, 4, 4);
                switch (ByteConverTools.boolenArrayToByte(data)){
                    case 64 : return ModbusEnum.DOOR_CLOSE;
                    case 96 : return ModbusEnum.DOOR_CLSE_STOP;
                    case -128 : return ModbusEnum.DOOR_OPEN;
                    case -96 :return ModbusEnum.DOOR_OPEN_STOP;
                    case -120:return ModbusEnum.DOOR_AUTO_OPEN;
                    case -88:return ModbusEnum.DOOR_AUTO_OPEN_STOP;
                    case 68:return ModbusEnum.DOOR_AUTO_CLOSE;
                    case 100:return ModbusEnum.DOOR_AUTO_CLOSE_STOP;
                    case 40:return ModbusEnum.DOOR_AUTO_OPEN_STOP_SET;
                    case 36:return ModbusEnum. DOOR_AUTO_CLOSE_STOP_SET;
                    case 32:return ModbusEnum.STOP;
                    default:return ModbusEnum.NULL;
                }
            } catch (ModbusTransportException e) {
                logger.warn(e.getMessage());
                return ModbusEnum.ERR_OUTTIME;
            }
        }else {
            return ModbusEnum.ERR_OUTTIME;
        }
    }
    public Boolean openDoorStata(){
        if (NettyServerHandler.TestMastHost(nodeId)){
            byte data = 0x01;
            boolean[] bdate = ByteConverTools.ByteToArrayBoolen(data);
            try {
                return ModbusUtit.writeCoils(0x11,0x00,modbusMaster,bdate);
            } catch (ModbusTransportException e) {
                logger.warn(e.getMessage());
                return false;
            }
        }else {
            return false;
        }
    }
    public Boolean shutDoorState(){
        if (NettyServerHandler.TestMastHost(nodeId)){

            byte data = 0x02;

            boolean[] bdate = ByteConverTools.ByteToArrayBoolen(data);
            try {
                return ModbusUtit.writeCoils(0x11,0x00,modbusMaster,bdate);
            } catch (ModbusTransportException e) {
                logger.warn(e.getMessage());
                return false;
            }
        }else {
            return false;
        }
    }

    public Boolean resetDoorState(){
        if (NettyServerHandler.TestMastHost(nodeId)){
            byte data = 0x00;
            boolean[] bdate = ByteConverTools.ByteToArrayBoolen(data);
            try {
                return ModbusUtit.writeCoils(0x11,0x00,modbusMaster,bdate);
            } catch (ModbusTransportException e) {
                logger.warn(e.getMessage());
                return false;
            }
        }else {
            return false;
        }
    }


    public ModbusEnum getModbusEnum() {
        return modbusEnum;
    }

    public Future<ModbusEnum> getModbusEnumFuture() {
        return modbusEnumFuture;
    }

    public ModbusFactory getModbusFactory() {
        return modbusFactory;
    }

    public ModbusMaster getModbusMaster() {
        return modbusMaster;
    }

    public PriorityThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public List<String> getNodePoint() {
        return nodePoint;
    }
}
