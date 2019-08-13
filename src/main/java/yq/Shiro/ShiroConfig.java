package yq.Shiro;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {


    @Autowired
    private TokenService tokenService;

    @Bean
    public TokenService tokenService(){
        return new TokenService();
    }

    //常量一：表示是角色
    public static final String CONS_TYPE_ONE = "ROLE";

    //常量二：表示是权限
    public static final String CONS_TYPE_TWO = "PERM";

    /**
     * 权限管理 核心安全事务管理器
     * @param realm
     * @return
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager getManager(MyRealm realm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 使用自己的realm
        manager.setRealm(realm);
        return manager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> myFilters = new HashMap<>();
        myFilters.put("authFilter",new AuthFilter(tokenService()));
        shiroFilterFactoryBean.setFilters(myFilters);
        Map<String,String> map = new LinkedHashMap<>();
        //用户登录 自由访问
        map.put("/user/login","anon");
        map.put("/static/**","anon");
        //需要admin角色
        map.put("/api/testRole","authFilter["+CONS_TYPE_ONE+",admin]");
        //需要test权限才能访问
        map.put("/api/testPerms","authFilter["+CONS_TYPE_TWO+",test]");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/user/error");
        //其他的api请求都需要认证
        map.put("/api/**","authFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    /**
     * 下面的代码是添加注解支持 aop（用于解决注解不生效的原因
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 管理shirobean的生命周期
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 加入注解
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
