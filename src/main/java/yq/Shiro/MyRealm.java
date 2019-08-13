package yq.Shiro;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yq.entity.Test;
import yq.service.MySqlService;

import java.util.Arrays;

@Service
public class MyRealm extends AuthorizingRealm {


    @Autowired
    private MySqlService mySqlService;

    @Autowired
    private TokenService tokenService;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof NewAuthenticationToken;
    }


    /**
     * 保存角色和权限
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Long testId = (Long) principals.getPrimaryPrincipal();
        Test testById = mySqlService.getTestById(testId);
        if(testById == null){
            throw new IllegalArgumentException("错误的角色");
        }
        //在这里给用户角色进行授权
        //在这里拿到用户的信息 并且赋值角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //设置用户角色
        simpleAuthorizationInfo.addRole(testById.getRole());
        //添加角色的权限
        simpleAuthorizationInfo.addStringPermissions(Arrays.asList(testById.getTurisdiction().split(",")));
        return simpleAuthorizationInfo;
    }

    /**
     * 身份认证
     * @param auth
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
    	//因为我们在用户登录的时候传递的参数 主体就是电话号码
		String phone = auth.getPrincipal().toString();
		//证明用户信息的东西
		String token =  auth.getCredentials().toString();
		//因为我们传递的是json类型的Test对象
        String jsonTest = tokenService.parseJWT(token);
        Test test = JSON.parseObject(jsonTest, Test.class);
        if(! test.getPhone().equals(phone)){
            throw new AuthenticationException("用户身份验证失败");
        }
        //保存用户信息？test, token, "my_realm"
        return new SimpleAuthenticationInfo(test.getId(),token,"myRealm");
    }
}