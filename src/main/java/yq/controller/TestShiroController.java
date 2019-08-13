package yq.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yq.Shiro.AuthFilter;
import yq.Shiro.NewAuthenticationToken;
import yq.Shiro.TokenService;
import yq.commons.responseBase.BaseApiService;
import yq.commons.responseBase.ResponseBase;
import yq.entity.Test;
import yq.service.MySqlService;

@RestController
public class TestShiroController extends BaseApiService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MySqlService mySqlService;

    /*
   用户登录 需要传递用户邮箱和密码
    */
    @PostMapping(value = "/user/login")
    public ResponseBase login(@RequestBody Test test) {
        //根据id查询Test
        Test testById = mySqlService.getTestById(test.getId());
        //判断不能为null
        Assert.notNull(testById,"用户账号错误");
        //获取到加密之后的password
        String encryptionPassWord = tokenService.parseJWT(testById.getPassWord());
        //密码判断
        if(! test.getPassWord().equals(encryptionPassWord)){
            throw new IllegalArgumentException("密码错误");
        }
        Subject subject = SecurityUtils.getSubject();
        //设置登录token  过期时间为30分钟
        String token = tokenService.createToken(JSON.toJSONString(testById));
        //这个类 是我们继承与shiro的AuthenticationToken 这样就可以做一些定制化的东西
        NewAuthenticationToken newAuthenticationToken = new NewAuthenticationToken(testById.getPhone(), token);
        //登录操作
        subject.login(newAuthenticationToken);
        //返回客户端数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(AuthFilter.TOKEN, token);
        return setResultSuccessData(jsonObject.toString(), "用户登录成功");
    }

    @PostMapping(value = "/api/test001")
    public ResponseBase test001(){
        return setResultSuccess("测试登录成功");
    }

    //测试权限使用
    @PostMapping(value = "/api/testRole")
    public ResponseBase testRole(){
        return setResultSuccess("测试角色成功");
    }

    //测试权限使用
    @PostMapping(value = "/api/testPerms")
    public ResponseBase testPerms(){
        return setResultSuccess("测试权限成功");
    }

}
