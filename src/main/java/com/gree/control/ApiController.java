package com.gree.control;

import com.gree.configure.ModbusConfigure;
import com.gree.modbus.Modbus;
import com.gree.modbus.ModbusEnum;
import com.gree.modbus.ModbusMux;
import com.gree.netty.pipeline.MastbusToDecode;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller("/api")
public class ApiController {

    final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Inject
    ModbusConfigure configure;

    @Get
    public List<ModbusEnum> getDoorList(){
        List<ModbusEnum> nodeData = new ArrayList<>();
        for (Integer nodeId:configure.getListNode()){
            Modbus modbus = ModbusMux.modbuses.get(configure.getListNode().indexOf(nodeId));
            try {
                nodeData.add(modbus.read(10).get(4, TimeUnit.SECONDS)) ;
            } catch (InterruptedException e) {
                nodeData.add(ModbusEnum.ERR_OUTTIME);
                e.printStackTrace();
            } catch (ExecutionException e) {
                nodeData.add(ModbusEnum.ERR_OUTTIME);
                e.printStackTrace();
            } catch (TimeoutException e) {
                nodeData.add(ModbusEnum.ERR_OUTTIME);
                e.printStackTrace();
            }
        }
        return nodeData;
    }

    @Post
    public List<Boolean> openAllDoor(){
        List<Boolean> result = new ArrayList<>();
        for (Integer nodeId:configure.getListNode() ){
            Modbus modbus = ModbusMux.modbuses.get(configure.getListNode().indexOf(nodeId));
            try {
                result.add(modbus.open(12).get(4, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
                result.add(false);
            } catch (ExecutionException e) {
                e.printStackTrace();
                result.add(false);
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Delete
    public List<Boolean> resetAllDoor(){
        List<Boolean> result = new ArrayList<>();
        for (Integer nodeId:configure.getListNode() ){
            Modbus modbus = ModbusMux.modbuses.get(configure.getListNode().indexOf(nodeId));
            try {
                result.add( modbus.reset(11).get(4, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
                result.add(false);
            } catch (ExecutionException e) {
                e.printStackTrace();
                result.add(false);
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Patch
    public List<Integer> pathDooor(){
        return configure.getListNode();
    }

    @Put
    public List<Boolean> ShutDoor(){
        List<Boolean> result = new ArrayList<>();
        for (Integer nodeId:configure.getListNode() ){
            Modbus modbus = ModbusMux.modbuses.get(configure.getListNode().indexOf(nodeId));
            try {
                result.add(modbus.close(10).get(4, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
                result.add(false);
            } catch (ExecutionException e) {
                e.printStackTrace();
                result.add(false);
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Delete("{nodeId}")
    public Boolean resetDoor(@PathVariable Integer nodeId){
        Modbus modbus = ModbusMux.modbuses.get(configure.getListNode().indexOf(nodeId));
        try {
            return modbus.reset(11).get(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (TimeoutException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Get("{nodeId}")
    public ModbusEnum getDoorState(@PathVariable Integer nodeId){
        if (configure.getListNode().contains(nodeId)){
            Modbus modbus = ModbusMux.modbuses.get(configure.getListNode().indexOf(nodeId));
            try {
                return modbus.read(10).get(4, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return ModbusEnum.ERR_OUTTIME;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return ModbusEnum.ERR_OUTTIME;
            } catch (TimeoutException e) {
                e.printStackTrace();
                return ModbusEnum.ERR_OUTTIME;
            }
        }else {
            return ModbusEnum.ERR_OUTTIME;
        }
    }

    @Post("{nodeId}")
    public Boolean openDoor(@PathVariable Integer nodeId){
        Modbus modbus = ModbusMux.modbuses.get(configure.getListNode().indexOf(nodeId));
        try {
            return modbus.open(12).get(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (TimeoutException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Put("{nodeId}")
    public Boolean shutDoor(@PathVariable Integer nodeId){
        Modbus modbus = ModbusMux.modbuses.get(configure.getListNode().indexOf(nodeId));
        try {
            return modbus.close(10).get(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (TimeoutException e) {
            e.printStackTrace();
            return false;
        }
    }
}
