package yq.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yq.Shiro.TokenService;

import yq.commons.idUtils.IdUtil;
import yq.commons.responseBase.BaseApiService;
import yq.commons.responseBase.ResponseBase;
import yq.entity.Test;
import yq.service.MySqlService;


@RestController
public class UserController extends BaseApiService {

    @Autowired
    private TokenService tokenService;

    //我的数据库访问层
    @Autowired
    private MySqlService mySqlService;

//    //用于生产token和验证token的
//    @Autowired
//    private LoginTokenService loginTokenService;
//
//    //SpringSecurity登录授权
//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;
//
//    @PostMapping(value = "/userLogin")
//    public ResponseBase login(@RequestBody Test test){
//        Test testById = mySqlService.getTestById(test.getId());
//        if(testById == null){
//            return setResultError("用户账号错误");
//        }
//        if(! testById.getPassWord().equals(test.getPassWord())){
//            return setResultError("密码错误");
//        }
//        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
//        userDetailsService.loadUserByUsername(testById.getId().toString());
//        String loginToken = loginTokenService.creaetToken(testById.getId().toString(), testById.getPassWord());
//        objectObjectHashMap.put("loginToken",loginToken);
//        objectObjectHashMap.put("userName",testById.getUserName());
//        return setResultSuccessData(JSON.toJSONString(objectObjectHashMap),"登陆成功");
//    }

    @PostMapping(value = "/api/test")
    public ResponseBase test(){
        return setResultSuccess("测试成功");
    }


    @PostMapping(value = "/api/testTwo")
    public ResponseBase testTwo(){
        return setResultSuccess("测试成功");
    }


    @PostMapping(value = "/saveTest")
    public ResponseBase saveTest(@RequestBody Test test){
        test.setId(IdUtil.createId());
        //密码加密
        test.setPassWord(tokenService.passWordEncryption(test.getPassWord()));
        mySqlService.addTest(test);
        return setResultSuccess("新增用户成功");
    }

}
