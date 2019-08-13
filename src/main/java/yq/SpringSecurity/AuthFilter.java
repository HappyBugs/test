//package yq.SpringSecurity;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
////只执行一次 用于在用户登录验证之前进行验证
//@Component
//public class AuthFilter extends OncePerRequestFilter {
//
//
//    //我们验证的授权token的key
//    private final static String AUTHORIZATIONHEAD = "loginToken";
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private LoginTokenService loginTokenService;
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        //如果当前的用户没有身份令牌 表示没有登录
//        if (SecurityContextHolder.getContext().getAuthentication() == null) {
//            //调用授权方法
//            Authentication rememberMeAuth = authentication(request, response);
//            if (rememberMeAuth != null) {
//                // 如果授权成功
//                try {
//                    //进行授权验证
//                    rememberMeAuth = authenticationManager.authenticate(rememberMeAuth);
//                    // 保存值
//                    SecurityContextHolder.getContext().setAuthentication(rememberMeAuth);
//                } catch (AuthenticationException authenticationException) {
//                    logger.error("认证失败 ex{} ", authenticationException);
//                }
//            }
//        }
//        //开始下一个过滤器
//        filterChain.doFilter(request, response);
//    }
//
//    //授权的方法
//    public Authentication authentication(HttpServletRequest request,HttpServletResponse response){
//        //得到请求中的 token
//        String loginToken = request.getHeader(AUTHORIZATIONHEAD);
//        if(StringUtils.isEmpty(loginToken)){
//            return null;
//        }
//        //验证token
//        return loginTokenService.check(loginToken,request,response);
//    }
//
//}
