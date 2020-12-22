package com.gree.modbus;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.*;

public class ModbusUtit {
    /**
     * 读（线圈）开关量数据
     *
     * @param slaveId slaveId
     * @param offset  位置
     * @return 读取值
     */
    public static boolean[] readCoilStatus(int slaveId, int offset, ModbusMaster master, int numberOfBits)
            throws ModbusTransportException {

        ReadCoilsRequest request = new ReadCoilsRequest(slaveId, offset, numberOfBits);
        ReadCoilsResponse response = (ReadCoilsResponse) master.send(request);
        boolean[] booleans = response.getBooleanData();
        return valueRegroup(numberOfBits, booleans);

    }
    /**
     * 开关数据 读取外围设备输入的开关量
     */
    public static boolean[] readInputStatus(int slaveId, int offset, ModbusMaster master,int numberOfBits)
            throws ModbusTransportException {
        ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, offset, numberOfBits);
        ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) master.send(request);
        boolean[] booleans = response.getBooleanData();
        return valueRegroup(numberOfBits, booleans);
    }
    /**
     * 写多个开关量数据（线圈）
     *
     * @param slaveId     slaveId
     * @param startOffset 开始位置
     * @param bdata       写入的数据
     * @return 是否写入成功
     */
    public static boolean writeCoils(int slaveId, int startOffset, ModbusMaster master,boolean[] bdata)
            throws ModbusTransportException{
        // 创建请求
        WriteCoilsRequest request = new WriteCoilsRequest(slaveId, startOffset, bdata);
        // 发送请求并获取响应对象
        WriteCoilsResponse response = (WriteCoilsResponse) master.send(request);
        return !response.isException();
    }

    private static boolean[] valueRegroup(int numberOfBits, boolean[] values) {
        boolean[] bs = new boolean[numberOfBits];
        int temp = 1;
        for (boolean b : values) {
            bs[temp - 1] = b;
            temp++;
            if (temp > numberOfBits) {
                break;
            }
        }
        return bs;
    }
}
