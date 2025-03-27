package com.xcr.orange.oa.util;

import cn.hutool.core.util.URLUtil;
import com.google.gson.JsonObject;
import com.squareup.okhttp.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * descripion: httpClient请求helper
 *
 * @author yshi on 2017/6/9
 */
public class HttpClientHelper {

    /**
     * 日志.
     */
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientHelper.class);

    /**
     * json post请求JSON解析.
     */
    private static final MediaType JSON_POST = MediaType.parse("application/json; charset=utf-8");
    /**
     * 连接超时时长，单位秒
     */
    private static final long CONNECT_TIMEOUT = 30L;
    /**
     * 读取超时时长，单位秒
     */
    private static final long READ_TIMEOUT = 30L;
    /**
     * 写入超时时长，单位秒
     */
    private static final long WRITE_TIMEOUT = 30L;
    /**
     * http客户端
     */
    private static OkHttpClient okHttpsClient = null;

    static {
        // 初始化OkHttpClient
        okHttpsClient = new OkHttpClient();
        // 连接超时
        okHttpsClient.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        // 读取超时
        okHttpsClient.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        // 写入超时
        okHttpsClient.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        // 自动重连
        okHttpsClient.setRetryOnConnectionFailure(true);
    }

    /**
     * 发送get请求，默认utf-8编码
     *
     * @param url      请求url
     * @param paramMap 参数列表
     * @return 请求返回结果
     */
    public static Pair<Boolean, String> get(String url, Map<String, String> paramMap) {
        //  发送get请求
        return getRequest(url, paramMap);
    }

    /**
     * 发送get请求，默认utf-8编码
     *
     * @param url 请求url
     * @return 请求返回结果
     */
    public static Pair<Boolean, String> get(String url) {
        //  发送get请求
        return getRequest(url, null);
    }

    /**
     * 发送get请求
     *
     * @param url      请求url
     * @param paramMap 参数列表
     * @return 请求返回结果
     */
    public static Pair<Boolean, String> getRequest(String url, Map<String, String> paramMap) {
        //  判空
        if (StringUtils.isBlank(url)) {
            LOG.error("Url不能为空!");
            return null;
        }
        //  设置参数
        if (null != paramMap) {
            StringBuilder builder = new StringBuilder("?");
            for (Map.Entry<String, String> elem : paramMap.entrySet()) {
                builder.append(elem.getKey()).append("=").append(elem.getValue()).append("&");
            }
            String paramStr = builder.toString();
            url += paramStr.substring(0, paramStr.lastIndexOf("&"));
        }
        Request request = new Request.Builder().url(url).get().build();
        return sendRequest(request);
    }

    /**
     * 发送post请求
     *
     * @param url      请求url
     * @param paramMap 参数map
     * @return
     */
    public static Pair<Boolean, String> post(String url, Map<String, String> paramMap) {
        try {
            return postRequest(url, paramMap);
        } catch (Exception e) {
            return Pair.of(false, ExceptionUtils.getStackTrace(e));
        }
    }

    public static Pair<Boolean, String> post(String url, String json) {
        RequestBody body = RequestBody.create(JSON_POST, json);
        Request request = new Request.Builder().url(url).post(body).build();
        return sendRequest(request);
    }

    /**
     * 发送post请求
     *
     * @param url 请求url
     * @return
     */
    public static Pair<Boolean, String> post(String url) {
        try {
            return postRequest(url, null);
        } catch (Exception e) {
            return Pair.of(false, ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 发送post请求
     *
     * @param url      请求的url
     * @param paramMap 参数map
     * @return
     */
    private static Pair<Boolean, String> postRequest(String url, Map<String, String> paramMap) {
        if (StringUtils.isBlank(url)) {
            return Pair.of(false, "Url不能为空.");
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        //设置参数
        if (null != paramMap) {
            for (Map.Entry<String, String> elem : paramMap.entrySet()) {
                builder.add(elem.getKey(), elem.getValue());
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        return sendRequest(request);
    }


    /**
     * 接口成功代码
     */
    private static final String SUCCESS_CODE = "600000";

    /**
     * 提交任务请求
     * @param url 请求地址
     * @param jsonString 任务参数信息
     * @return key:是否成功 val:具体信息
     */
    public static Pair<Boolean, String> postJsonToFdp(String url, String jsonString) {
        Pair<Boolean, String> pair = postJsonRequest(url, jsonString);
        //  获取返回JSON对象
        if (pair.getKey()) {
            JsonObject asJsonObject = GsonUtil.JSON_PARSER.parse(pair.getValue()).getAsJsonObject();
            boolean r = SUCCESS_CODE.equals(asJsonObject.get("code").getAsString());
            if (r) {
                LOG.info("提交任务成功：{}", asJsonObject.get("data").getAsString());
                return pair;
            }else {
                LOG.error("提交任务失败！请检查请求地址:{}  错误信息：{}", url, pair.getValue());
                return Pair.of(false,String.format("提交任务失败！请检查请求地址:%s  错误信息：%s", url, pair.getValue()));
            }
        } else {
            LOG.error("提交任务失败！请检查请求地址:{}  错误信息：{}", url, pair.getValue());
            return Pair.of(false,String.format("提交任务失败！请检查请求地址:%s  错误信息：%s", url, pair.getValue()));
        }

    }


    /**
     * 发送json post请求
     *
     * @param url 请求的url
     * @return
     */
    private static Pair<Boolean, String> postJsonRequest(String url, String jsonString) {
        RequestBody formBody = RequestBody.create(JSON_POST, jsonString);
        try {
            Request request = new Request.Builder().url(url).post(formBody).build();
            return sendRequest(request);
        } catch (Exception e) {
            return Pair.of(false, ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 发送请求
     *
     * @param request 请求
     * @return 返回结果
     */
    private static Pair<Boolean, String> sendRequest(Request request) {
        ResponseBody responseBody = null;
        try {
            responseBody = okHttpsClient.newCall(request).execute().body();
            return Pair.of(true, responseBody.string());
        } catch (IOException e) {
            return Pair.of(false, ExceptionUtils.getStackTrace(e));
        } finally {
            if (responseBody != null) {
                try {
                    responseBody.close();
                } catch (IOException ignore) {
                    LOG.error("", ignore);
                }
            }
        }
    }

    /**
     * 测试服务连接
     *
     * @param name
     * @param url
     * @param timeout
     * @return
     */
    public static Pair<Boolean, String> testUrl(String name, String url, int timeout) {
        if (StringUtils.isBlank(url)) {
            return Pair.of(false, String.format("服务【%s】地址未配置", name));
        }
        URI uri;
        try {
            uri = URLUtil.toURI(url);
        } catch (Exception e) {
            return Pair.of(false, String.format("服务【%s】地址未配置正确：%s", name, url));
        }
        String host = uri.getHost();
        int port = uri.getPort();

        Socket s = new Socket();
        try {
            s.connect(new InetSocketAddress(host, port), timeout);
            return Pair.of(true, String.format("服务【%s】正常", name));
        } catch (IOException e) {
            return Pair.of(false, String.format("服务【%s】已断开", name));
        } catch (Exception e) {
            return Pair.of(false, String.format("服务【%s】连接异常：%s", name, e.getMessage()));
        } finally {
            try {
                s.close();
            } catch (IOException ex) {
                LOG.warn("关闭test服务socket异常");
            }
        }
    }
}