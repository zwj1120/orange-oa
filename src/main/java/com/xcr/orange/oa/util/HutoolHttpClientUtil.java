package com.xcr.orange.oa.util;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fiberhome.daml.common.standard.exception.ExceptionBuilder;
import com.xcr.orange.oa.common.error.ExamErrorCode;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.tuple.Pair;


/**
 * hutool httpClient 工具类
 *
 * @author ZhangDong X7450
 * @date 2022-11-02 15:55
 */
public class HutoolHttpClientUtil {

    /**
     * fdp接口成功代码
     */
    private static final String FDP_SUCCESS_CODE = "0";


    public static Pair<Boolean, String> postJson(String url, String paramJson) {
        try {
            HttpResponse execute = HttpRequest.post(url).contentType(ContentType.JSON.toString())
                    .body(paramJson).execute();
            String post = execute.body();
            JsonObject asJsonObject = GsonUtil.JSON_PARSER.parse(post).getAsJsonObject();
            boolean r = FDP_SUCCESS_CODE.equals(asJsonObject.get("code").getAsString());
            if (r) {
                return Pair.of(true, post);
            } else {
                String msg = asJsonObject.get("msg").getAsString();
                return Pair.of(false, String.format("提交任务失败！请检查请求地址:%s  错误信息：%s", url, msg));
            }
        } catch (Exception e) {
            throw ExceptionBuilder.systemException(ExamErrorCode.SYSTEM_ERROR).errorMessage("调用接口【{0}】异常，请求参数：【{1}】")
                    .params(url, paramJson).build();
        }
    }


}
