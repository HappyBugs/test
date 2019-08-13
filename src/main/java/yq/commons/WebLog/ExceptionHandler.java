package yq.commons.WebLog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import yq.commons.responseBase.BaseApiService;
import yq.commons.responseBase.ResponseBase;


@Slf4j
@ResponseBody
public class ExceptionHandler extends BaseApiService {

    //截取异常关键信息
    protected String errorDescribe(Throwable throwable){
        String name = throwable.getClass().getName();
        String message = throwable.getMessage();
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];
        String methodName = stackTraceElement.getMethodName();
        String className = stackTraceElement.getClassName();
        int lineNumber = stackTraceElement.getLineNumber();
        String format = String.format("异常描述----> 程序在执行到 %s 中的 %s 方法第 %s 行的时候报错：%s", className, methodName, lineNumber, message);
        return format;
    }

    //统一返回
    protected ResponseBase unifiedReturn(Throwable throwable,String message){
        log.error(errorDescribe(throwable));
        ResponseBase responseBase = setResultError(message);
        log.info("异常响应----> {}",responseBase);
        return responseBase;
    }

}
