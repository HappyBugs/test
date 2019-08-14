package yq.Shiro;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import yq.entity.Test;
import yq.entity.UserToken;
import yq.service.UserTokenService;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * 用于角色身份验证
 */
@Slf4j
public class AuthFilter extends AuthorizationFilter {




    private final UserTokenService userTokenService;


    //token
    private final TokenService tokenService;


    public AuthFilter(TokenService tokenService,UserTokenService userTokenService){
        this.tokenService = tokenService;
        this.userTokenService = userTokenService;
        System.out.println(tokenService);
    }

    public static final String TOKEN = "token";

    private Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    /**
     * 身份认证方法
     */
    private Boolean authorization(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = request.getHeader("token");
            Assert.hasLength(token,"请输入正确的token");
            String testJson = tokenService.parseJWT(token);
            Test test = JSON.parseObject(testJson, Test.class);
            Assert.notNull(test,"请输入正确的token");
            //到了这里说明身份验证成功了，token是没有问题的
//            String threadToken = myThreadLocal.get();
//            if(StringUtils.isEmpty(threadToken)){
//                //查询数据库 得到token
//                UserToken userTokenByToken = userTokenService.getUserTokenByToken(token);
//                myThreadLocal.set(userTokenByToken.getToken());
//                threadToken = myThreadLocal.get();
//            }
//            if(! threadToken.equals(token)){
//                throw new IllegalArgumentException("与当前登录用户不一致");
//            }
            Subject subject = getSubject();
            //表示还没有登录  为什么这里要这么写 就是因为shiro我们的缓存是基于session，cookie等
            //如果服务重启了 或者没了cookiet咋办，所以我们就在这里掉用一下shiro的登录
            if(subject.getPrincipal() == null){
                //那就登录
                subject.login(new NewAuthenticationToken(test.getId(),token));
                return true;
            }
            //如果是已经登录的 就进行身份验证
            Long testId = (Long) subject.getPrincipal();
            if(! test.getId().equals(testId)){
                throw new IllegalArgumentException("与当前登录身份不一致");
            }
            return true;
        } catch (Exception e) {
            log.error("用户验证失败的地址:{}",request.getRequestURL());
            log.error("错误原因:{}",e.getMessage());
            response.setHeader("message",e.getMessage());
            return false;
        }
    }

    /**
     * 认证失败
     * @param response
     */
    private void authorizationFailure(HttpServletResponse response){
        try{
            //认证失败 之后返回页面的数据
            response.setContentType("application/json;charset=utf-8");
            //封装一个map返回页面
            HashMap<Object, Object> result = new HashMap<>();
            result.put("data","null");
            result.put("message",response.getHeader("message"));
            result.put("rtnCode","401");
            response.getWriter().append(JSON.toJSONString(result));
        }catch (Exception e){
            log.error("响应错误：{}",e.getMessage());
        }
    }

    /**
     * 权限认证的方法
     * @param perms
     * @param response
     * @param request
     * @return
     */
    private Boolean permissions(String[] perms,HttpServletResponse response,HttpServletRequest request){
        Boolean result = false;
        try{
            Subject subject = getSubject();
            //调用方法进行判断权限
            if(! subject.isPermittedAll(perms)){
                throw new Exception("您没有该访问权限");
            }
            result = true;
        }catch (Exception e){
            log.error("角色不对应导致无访问权限的地址:{}",request.getRequestURL());
            log.error("错误原因:{}",e.getMessage());
            response.setHeader("message",e.getMessage());
        }finally {
            return result;
        }
    }

    //角色认证认证
    private Boolean roles(List<String> roles, HttpServletResponse response, HttpServletRequest request){
        Boolean result = false;
        try{
            if(roles == null || roles.size() <= 0){
                return true;
            }
            Subject subject = getSubject();
            //调用方法判断 是否存在指定角色
            if(! subject.hasAllRoles(roles)){
                throw new Exception("没有该访问权限");
            }
            result = true;
        }catch (Exception e){
            log.error("没有权限的访问地址:{}",request.getRequestURL());
            log.error("错误原因:{}",e.getMessage());
            response.setHeader("message",e.getMessage());
        }finally {
            return result;
        }
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String[] values = (String[]) mappedValue;
        HttpServletRequest newRequest = (HttpServletRequest) request;
        HttpServletResponse newResponse = (HttpServletResponse) response;
        //身份认证
        if(values == null){
            return authorization(newRequest,newResponse);
        }
        //表示是角色认证
        if(values[0].equals(ShiroConfig.CONS_TYPE_ONE)){
            //因为我们使用asList转换代码为List的时候不是util包下面的List而是array下面的，
            // 所以我们需要转换为util包下的，才能执行remove方法
            //那么为什么我们需要删除第0个呢？就是因为我们在shiroConfig的时候配置的过滤策略
            //因为我们的自定的authFilte需要执行的认证种类太多，所以需要第一个参数进行判断类型，
            //但是这第零个参数又是属于权限和角色范围，所以在类型判断之后需要删除
            List<String> strings = Arrays.asList(values);
            List<String> params =  new ArrayList<>(strings);
            params.remove(0);
            //调用角色认证方法
            return roles(params,newResponse,newRequest);
        }
        //权限认证
        if(values[0].equals(ShiroConfig.CONS_TYPE_TWO)){
            //同上 一样的意思
            List<String> strings = Arrays.asList(values);
            List<String> params =  new ArrayList<>(strings);
            params.remove(0);
            //因为 我们的权限认证方法的参数是需要的是 String... 类型
            // 但是String[] 有没有删除第一个的实现，所以就比较麻烦先转换list删除第一个，然后又转换回去
            String[] perm = new String[params.size()];
            String[] newPerm = params.toArray(perm);
            //调用权限认证方法
            return permissions(newPerm,newResponse,newRequest);
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        //如果权限或者角色也或者是角色认证不通过
        authorizationFailure((HttpServletResponse) response);
        return false;
    }
}
