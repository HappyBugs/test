//package yq.SpringSecurity;
//
//import com.alibaba.fastjson.JSON;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.RememberMeAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import yq.commons.responseBase.BaseApiService;
//
//import javax.sql.DataSource;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//
////lombok 注解 简化log日志的
//@Slf4j
////不使用默认的SpringSecurity 使用我们自定义的
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
//public class AuthorizationConfiguration extends WebSecurityConfigurerAdapter {
//
//    private static final String key = "key";
//
//    //进行权限认证的filter过滤器
//    @Autowired
//    private AuthFilter authFilter;
//
//    //数据源
//    @Autowired
//    private DataSource datasource;
//
//    //
//    @Autowired
//    private BaseApiService baseApiService;
//
//
//    @Autowired
//    private DIYAuthenticationEntryPoint diyAuthenticationEntryPoint;
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    //密码加密
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    //用户身份验证
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.
//                //自定义的验证
//                        userDetailsService(userDetailsServiceImpl())
//                .and()
//                .jdbcAuthentication()
//                //密码加密方式
//                .passwordEncoder(bCryptPasswordEncoder())
//                .dataSource(datasource)
//                .and()
//                .authenticationProvider(new RememberMeAuthenticationProvider(key));
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                //跨域支持
//                .cors()
//                .and()
//                //这个一定要关闭 不然的话使用post的请求会把报错 好像不关闭只能使用get请求
//                .csrf().disable()
//                //配置认证失败处理
//                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler())
//                .and()
//                //session
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                //开始授权的地方
//                .authorizeRequests()
//                //这些url可以不需要认证 直接访问
//                .antMatchers("/static/**","/userLogin","/error","/saveTest").permitAll()
//                //这个接口需要user权限
//                .antMatchers("/api/testTwo").hasAuthority("user")
//                //这个接口需要 admin权限
//                .antMatchers("/api/**").hasAuthority("admin")
////                .anyRequest().hasAuthority("admin")
//                .and()
//                //在验证之前执行  也就是使用我们自己定义的filter过滤器进行用户验证
//                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
//                //禁止页面缓存
//                .headers().frameOptions().sameOrigin().cacheControl();
//    }
//
//    //没有授权的人（验证不通过
//    public AuthenticationEntryPoint unauthorizedHandler() {
//        // lambda
//        return (request, response, authException) -> {
//            log.error("auth error query path {}", request.getRequestURL());
//            response.setContentType("application/json;charset=utf-8");
//            //封装一个map返回页面
//            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
//            objectObjectHashMap.put("data","null");
//            objectObjectHashMap.put("message","您没有访问权限");
//            objectObjectHashMap.put("rtnCode","403");
//            response.getWriter().append(JSON.toJSONString(objectObjectHashMap));
//        };
//    }
//
//    //跨域
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        final CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Collections.singletonList("*"));
//        //允许访问的请求类型
//        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//        //是否支持用户凭证
//        configuration.setAllowCredentials(true);
//        // setAllowedHeaders is important! Without it, OPTIONS preflight request
//        // will fail with 403 Invalid CORS request
//        //允许header中携带的内容  loginToken：这是我们登录的token的名称
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "loginToken"));
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        //所有请求都使用 CorsConfiguration
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    //注入到容器  为什么需要在这里手动的注入 不适用注解 @Component等注入呢
//    //因为在 UserDetailsServiceImpl 中有其他的依赖项 如果在使用 @Component 注入
//    //那么SpringSecurity 还没有加载完成就会报错
//    @Bean
//    public UserDetailsServiceImpl userDetailsServiceImpl(){
//        return new UserDetailsServiceImpl();
//    }
//
//    //同上原理
//    @Bean
//    public LoginTokenService loginTokenService() {
//        return new LoginTokenService("key",userDetailsServiceImpl());
//    }
//
//}
