package com.xcr.orange.oa.common.error;

import com.fiberhome.daml.common.standard.error.code.ErrorCode;
import com.fiberhome.daml.common.standard.error.code.ErrorCode.ErrorType;
import com.fiberhome.daml.common.standard.error.code.ErrorCode.Module;

/**
 * @author tanxj
 * @createTime 2022/10/28
 * <p>
 * 探查工具错误码定义类
 */
public class ExamErrorCode {
    private ExamErrorCode() {
    }

    /**
     * 根据传入模块名和错误类型，返回一个ErrorCode类型对象，其中应用写死为ASSETS
     */
    public static ErrorCode createErrorCode(Module module, ErrorType errorType) {
        return ErrorCode.of(ErrorCode.Application.ASSETS, module, errorType);
    }

    /**
     * 文件解析异常
     */
    public static final ErrorCode FILE_PARSE_ERROR = createErrorCode(Module.RESOURCE_MGR, ErrorType.B0300);
    /**
     * 数据库执行SQL异常
     */
    public static final ErrorCode SQL_EXECUTE_ERROR = createErrorCode(Module.RESOURCE_MGR, ErrorType.C0120);

    /**
     * 参数异常
     */
    public static final ErrorCode PARAM_ERROR = createErrorCode(Module.RESOURCE_MGR, ErrorType.A0202);

    /**
     * 系统执行出错
     */
    public static final ErrorCode SYSTEM_ERROR = createErrorCode(Module.RESOURCE_MGR, ErrorType.B0000);

    /**
     * 调用盘古元数据服务报错
     */
    public static final ErrorCode PANGU_SYSTEM_ERROR = ErrorCode.of(ErrorCode.Application.ASSETS,
            Module.RESOURCE_MGR, ErrorType.B0000);

    /**
     * 生成任务异常
     */
    public static final ErrorCode CREATE_TASK_ERROR = createErrorCode(Module.TASK_MGR, ErrorType.B0000);

    /**
     * 配置文件异常
     */
    public static final ErrorCode CONFIG_FILE_ERROR = createErrorCode(Module.CONFIG_MGR, ErrorType.B0300);

    /**
     * 查询数据异常
     */
    public static final ErrorCode QUERY_DATA_ERROR = createErrorCode(Module.RESOURCE_MGR, ErrorType.C0130);


}
