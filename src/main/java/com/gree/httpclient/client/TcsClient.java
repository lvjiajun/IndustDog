package com.gree.httpclient.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gree.configure.ModbusConfigure;
import com.gree.configure.TcsConfiguration;
import com.gree.control.ApiController;
import com.gree.httpclient.entity.TcsEntity;
import com.gree.modbus.Modbus;
import com.gree.modbus.ModbusMux;
import io.micronaut.context.annotation.Context;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * @author Cloudjumper
 */
@Context
public class TcsClient {

    final Logger logger = LoggerFactory.getLogger(TcsClient.class);

    @Inject
    private OkHttpClient okHttpClient;

    @Inject
    ModbusConfigure modbusConfigure;

    @Inject
    ApiController apiController;

    @Inject
    TcsConfiguration tcsConfiguration;

    List<TcsEntity> tcsEntities;
    ScheduledExecutorService scheduledExecutorService;
    ThreadPoolExecutor threadPoolExecutor;
    TcsErrCode tcsErrCode;

    public Response get(String url, Map<String, String> queries) throws IOException {
        StringBuilder sb = new StringBuilder(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        Request request = new Request
                .Builder()
                .url(sb.toString())
                .build();
        return okHttpClient.newCall(request).execute();
    }

    public TcsClient() {

        scheduledExecutorService = Executors.newScheduledThreadPool(10);

        threadPoolExecutor = new ThreadPoolExecutor(
                10,
                10,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20));

        scheduledExecutorService.scheduleWithFixedDelay(
                (Runnable) this::tcsClientGet,
                1000,
                1500,
                TimeUnit.MILLISECONDS);
    }
    public Boolean tcsStopAgv(TcsEntity tcsEntity,Modbus modbus) throws IOException {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("vehicleName",tcsEntity.getName());param.put("rowCommand","A9C202006DAA");
        Response response = get("http://10.2.29.147:55200/v1/sendRowCommand",param);
        if ((response!=null)&&(response.code() == 200)&&(response.body()!=null)){

            if ("ok".equals(response.body().string())){

                logger.error("SEND AGV STOP {} ERR DOOR {}",tcsEntity.getName(),modbus.getNodeId());
                return true;
            }else {
                logger.error("AGV ERR STOP {}",tcsEntity.getName());
                return false;
            }
        }else {

            tcsErrCode = TcsErrCode.TCS_ERR_STOP;

            logger.error("发送AGV停止出现错误开启所有门 NODE_ID{},ERR_CODE{}",tcsEntity.getName(),response.code());

            apiController.openAllDoor();
            return false;

        }
    }
    public void tcsClientDel(List<TcsEntity> tcsEntities,Modbus modbus){
        for (TcsEntity tcsEntity:tcsEntities){
            for (String point:modbus.getNodePoint()){
                try {
                    if (point.equals(tcsEntity.getNextPosition()) ||
                            point.equals(tcsEntity.getCurrentPosition())){

                        if(!modbus.coil(0,13).get(3,TimeUnit.SECONDS)){
                            modbus.open(13);
                        }else {
                            logger.info("DOOR NODE ID HAVE OPEN {} car id {} NODE Get Door State",modbus.getNodeId(),tcsEntity.getName());
                        }
                        if ((!modbus.state(0,13).get(3,TimeUnit.SECONDS))){

                            logger.info("OPEN DOOR NODE ID {} car id {}",modbus.getNodeId(),tcsEntity.getName());

                            scheduledExecutorService.schedule(()->tcsClientOpen(modbus,tcsEntity),5,TimeUnit.SECONDS);

                        } else {
                            logger.info("DOOR NODE ID HAVE OPEN {} car id {}",modbus.getNodeId(),tcsEntity.getName());
                        }
                        return;
                    }else {

                    }
                } catch (InterruptedException e) {
                    logger.error("获取node InterruptedException"+e.getMessage());
                } catch (ExecutionException e) {
                    logger.error("获取node ExecutionException" + e.getMessage());
                } catch (TimeoutException e) {
                    try {
                        tcsStopAgv(tcsEntity,modbus);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    logger.error("获取node  TimeoutException"+e.getMessage());
                }
            }
        }
        logger.info("RESET DOOR NODE ID {}",modbus.getNodeId());
        modbus.reset(10);
    }
    public void tcsClientOpen(Modbus modbus,TcsEntity tcsEntity){
        {
            try {
                if (modbus.state(0,12).get(3,TimeUnit.SECONDS)){

                    logger.info("THE DOOR NOW IS OPEN NODEID {}",modbus.getNodeId());

                    return;

                }
                tcsStopAgv(tcsEntity,modbus);
            } catch (InterruptedException | ExecutionException | IOException |TimeoutException e) {

                tcsErrCode = TcsErrCode.TCS_ERR_STOP;

                apiController.openAllDoor();

                logger.error("发送AGV停止异常请求打开所有门"+e.getMessage());
            }
        }
    }
    public void tcsClientGet(){

        try {
            Response response = get("http://10.2.29.147:55200/v1/vehicles",null);

            if ((response!=null) && (response.code() == 200)&&(response.body() != null)){
                String json = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                tcsEntities = mapper.readValue(json, new TypeReference<List<TcsEntity>>() {});
                for (Modbus modbus : ModbusMux.modbuses){

                    threadPoolExecutor.submit(()->tcsClientDel(tcsEntities,modbus));

                }
                tcsErrCode = TcsErrCode.TCS_NORMAL;
            }else {

                logger.error("获取调度系统资源出现错误 开启所有门 code{} ,body{}",
                        response.code(),
                        response.body());
                apiController.openAllDoor();
                tcsErrCode = TcsErrCode.TCS_RETRUEERR;
            }
        } catch (IOException e) {
            tcsErrCode = TcsErrCode.TCS_JSON;
            apiController.openAllDoor();
            logger.error("获取调度系统JSON解析出现错误 开启所有门"+e.getMessage());
        }
    }
}