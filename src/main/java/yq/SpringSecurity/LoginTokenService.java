//package yq.SpringSecurity;
//
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//
////token服务 用于验证token和创建token
//public class LoginTokenService extends TokenBasedRememberMeServices {
//
//    public LoginTokenService(String key, UserDetailsService userDetailsService) {
//        super(key, userDetailsService);
//    }
//
//    //过期时间 20周
//    private final Long EXP_TIME = 1000L * TWO_WEEKS_S * 10;
//
//
//    //token验证是否合法
//    public Authentication check(String token, HttpServletRequest request, HttpServletResponse response) {
//        try {
//            //获得令牌
//            String[] tokens = decodeCookie(token);
//            //进行验证token 并且获得对象
//            UserDetails userDetails = processAutoLoginCookie(tokens, request, response);
//            return createSuccessfulAuthentication(request, userDetails);
//
//        } catch (Exception e) {
//            throw new IllegalArgumentException("错误的loginToken");
//        }
//    }
//
//
//    //创建用户token
//    public String creaetToken(String userName,String passWord){
//        //过期时间
//        Long exp = System.currentTimeMillis()+EXP_TIME;
//        String tokenSignature = makeTokenSignature(exp, userName, passWord);
//        return encodeCookie(new String[]{exp.toString(),userName,tokenSignature});
//    }
//
//}
