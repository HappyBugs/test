//package yq.SpringSecurity;
//
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//import yq.commons.WebLog.DIYException;
//import yq.commons.responseBase.BaseApiService;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
////这是自定义的授权失败的处理类 但是还没有使用
//@Component
//public class DIYAuthenticationEntryPoint extends BaseApiService implements AuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)  {
////        throw new DIYException(setResult(403,"您没有访问权限",null));
//        throw new DIYException("您还没有访问权限");
//    }
//
//}
