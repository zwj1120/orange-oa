package com.xcr.orange.oa.common.config;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * @author ZhangDong X7450
 * @version 1.0
 * @date 2023/8/28
 */
public class HttpServletUtils {

    /**
     * 判断是否为ajax
     *
     * @param request request
     * @return 是否为ajax请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        // 是否是上传接口
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        return "XMLHttpRequest".equals(request.getHeader("x-requested-with")) || isMultipart;
    }

    /**
     * 返回接口信息
     *
     * @param response response
     * @param status   状态码
     * @throws IOException IOException
     */
    public static void returnErrorMsg(HttpServletResponse response, int status) throws IOException {
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        writer.flush();
        writer.close();
    }
}
