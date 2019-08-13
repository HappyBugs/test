package yq.commons.WebLog;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import yq.commons.responseBase.ResponseBase;

@Component
@ControllerAdvice
public class Test extends ExceptionHandler {

    @Override
    public String errorDescribe(Throwable throwable) {
        return super.errorDescribe(throwable);
    }

    @Override
    protected ResponseBase unifiedReturn(Throwable throwable, String message) {
        return super.unifiedReturn(throwable, message);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DIYException.class)
    private ResponseBase diyException(DIYException diy){
        return unifiedReturn(diy,diy.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ArithmeticException.class)
    private ResponseBase ArithmeticException(ArithmeticException ar){
        return unifiedReturn(ar,"内部算术错误--"+ar.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Throwable.class)
    private ResponseBase Throwable(Throwable throwable){
        return unifiedReturn(throwable,"未处理的错误类型--"+throwable.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NullPointerException.class)
    private ResponseBase NullPointerException(NullPointerException n){
        return unifiedReturn(n,"内部空指针异常--"+n.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    private ResponseBase IllegalArgumentException(IllegalArgumentException ill){
        return unifiedReturn(ill,"非法参数--"+ill.getMessage());
    }


}
