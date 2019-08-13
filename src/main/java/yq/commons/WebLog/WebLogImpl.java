package yq.commons.WebLog;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;


import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Aspect
@Slf4j
@Component
public class WebLogImpl {


    private static JSONObject jsonObject;

    //切入点 Controller所有的方法
    @Pointcut("execution(* yq.controller..*.*(..))")
    private void webControllerPointcut() {
    }

    @Around(value = "webControllerPointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        //这三步就是获取HttpServletRequest对象是使用的
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        if(jsonObject == null){
            jsonObject = new JSONObject();
        }
        //字符串拼接
        StringBuffer requestURL = request.getRequestURL();
        //请求类型
        String method = request.getMethod();
        //content-type类型
        String header = request.getHeader("content-type");
        //所有参数值
        Object[] args = proceedingJoinPoint.getArgs();
        //得到对象
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        //获取所有参数名称
        String[] parameterNames = methodSignature.getParameterNames();
        //循环拼接参数字符串
        for(int i = 0 ; i < parameterNames.length ; i++){
            jsonObject.put(parameterNames[i],args[i]);
        }
        String paramJson = jsonObject.toJSONString();
        //保存运行结果对象
        Object proceed = null;
        try{
            //记录日志
            System.out.println("\n");
            log.info("发起请求----> url：{} <--> content-type: {} <--> method：{} <--> params：{}",requestURL.toString(),header,method,paramJson);
            //调用方法获得执行结果
            proceed = proceedingJoinPoint.proceed();
            log.info("响应请求----> url：{} <--> data：{}",requestURL.toString(),proceed);
            return proceed;
        }catch (Throwable th){
            //获取异常信息
            String message = th.getMessage();
            //得到异常全类名
            String name = th.getClass().getName();
            log.info("发生异常----> url：{} <--> errorType：{} <--> errorMessage：{}",requestURL.toString(),name,message);
            //抛出异常给全局异常处理
            throw th;
        }
    }

}