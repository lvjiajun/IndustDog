package com.gree.modbus;

import com.gree.configure.PriorityThreadPoolExecutor;
import com.gree.configure.ModbusConfigure;
import com.gree.configure.ThreadConfigure;
import com.gree.netty.server.NettyServerHandler;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.sun.org.apache.xpath.internal.operations.Mod;
import io.micronaut.context.annotation.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Context
public class ModbusMux implements Runnable{

    final Logger logger = LoggerFactory.getLogger(ModbusMux.class);

    @Inject
    ModbusConfigure configure;

    @Inject
    ThreadConfigure threadConfigure;

    public static ArrayList<Modbus> modbuses;

    private PriorityThreadPoolExecutor threadPoolExecutor;
    private ScheduledExecutorService scheduledExecutorService;

    @PostConstruct
    public void init() {
        modbuses = new ArrayList<>();
        scheduledExecutorService = Executors.newScheduledThreadPool(10);
        threadPoolExecutor = new PriorityThreadPoolExecutor(
                threadConfigure.corePoolSize,
                threadConfigure.maximumPoolSize,
                threadConfigure.keepAliveTime,
                TimeUnit.SECONDS,
                threadConfigure.capacity);

        Thread modbusThread = new Thread(this);
        modbusThread.start();
    }


    @PreDestroy
    public void destory() {

        threadPoolExecutor.shutdown();

        scheduledExecutorService.shutdown();

    }

    public void start(){
        if (configure.getListNode().size() ==
                configure.getListPort().size()){
            for (Integer port: configure.getListPort()){
                int index = configure.getListPort().indexOf(port);
                Integer nodeId = configure.getListNode().get(index);
                List<String> nodePoint = configure.getPoint().get(index);

                try {
                    Thread.sleep(500);
                    modbuses.add(new Modbus(port,nodeId,threadPoolExecutor,nodePoint));
                } catch (ModbusInitException | InterruptedException e) {
                    logger.error("占用"+e.getMessage());
                    return;
                }
            }
        }else {
            return;
        }
        scheduledExecutorService.scheduleWithFixedDelay(
                (Runnable) this::readTask, 5000, 5000, TimeUnit.MILLISECONDS);

    }

    public void readTask(){
        if ((NettyServerHandler.channelHandlerContext != null)
                &&(threadPoolExecutor.getPoolSize() < 10)){
            for (Modbus modbus:modbuses){
                modbus.modbusEnumFuture = modbus.read();
            }
        }else {
            logger.error("MATSER NODE ID HAVE DOWN OR NO CONNECT {}",configure.getMasterId());
        }
    }

    @Override
    public void run() {
        start();
    }

}
