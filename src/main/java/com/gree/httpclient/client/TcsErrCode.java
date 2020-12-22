package com.gree.httpclient.client;

public enum TcsErrCode {
    //请求超时
    TCS_TIMEOUT,
    //返回格式错误
    TCS_JSON,
    //没有正确返回OK
    TCS_RETRUEERR,
    //返回为空
    TCS_NULL,
    TCS_ERR_STOP,
    //
    TCS_NORMAL;
}
