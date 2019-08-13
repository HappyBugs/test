package yq.Shiro;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import yq.commons.WebLog.DIYException;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * 使用jjwt实现的token生成策略 以及密码加密策略
 */
@Slf4j
public class TokenService {

//	  "iss":"Issuer —— 用于说明该JWT是由谁签发的",
//    "sub":"Subject —— 用于说明该JWT面向的对象",
//    "aud":"Audience —— 用于说明该JWT发送给的用户",
//    "exp":"Expiration Time —— 数字类型，说明该JWT过期的时间",
//    "nbf":"Not Before —— 数字类型，说明在该时间之前JWT不能被接受与处理",
//    "iat":"Issued At —— 数字类型，说明该JWT何时被签发",
//    "jti":"JWT ID —— 说明标明JWT的唯一ID",
//    "user-definde1":"自定义属性举例",
//    "user-definde2":"自定义属性举例"

    //读取配置文件 秘匙 （这是用来后面接收到token的时候用于解密用的秘匙
    private String secretKey;

    //过期时间 两周
    private Long outTime_towWeeks;

    @Autowired
    private Environment environment;

    @PostConstruct
    private void inir() {
        this.secretKey = environment.getProperty("secretKey");
        Integer outTime = Integer.parseInt(environment.getProperty("outTime"));
        //过期时间两周
        this.outTime_towWeeks = outTime * 1000L * 60 * 60 * 24;
        log.info("JWTTokenUtil初始化完成，secretKey为：{} ，loginToken过期时间为：{}", secretKey, outTime_towWeeks);
    }

    /**
     * 字符串加密 如果参数type不是null 那么就是用户token生成。那是需要过期时间的
     * 如果type为null 那么就是密码加密 是不需过期时间的
     * @param subject 传递的字符串
     * @param type 需要加密类型 如果
     * @return
     */
    private String createJWT(String subject,String type) {
        //加密算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //创建jwt对象
        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .signWith(signatureAlgorithm, signingKey);
        //如果是null 就表示是密码加密 密码加密是不需要执行过期时间的
        if(StringUtils.isEmpty(type)){
            return builder.compact();
        }
        //设置两周之后过期
        builder.setExpiration(new Date(System.currentTimeMillis()+outTime_towWeeks));
        return builder.compact();
    }

    /**
     * token解密过程
     * @param jwtToken token
     * @return 解密后的值
     */
    public String parseJWT(String jwtToken) {
        try{Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(jwtToken).getBody();
            Date expiration = claims.getExpiration();
            //表示没有设置过期时间
            if(expiration == null){
                return claims.getSubject();
            }
            //表示已经过期
            if(System.currentTimeMillis() >= expiration.getTime()){
                throw new DIYException("token已经过期");
            }
            return claims.getSubject();
        }catch (DIYException d){
            throw d;
        }
        catch (Exception e){
            throw new DIYException("错误的token");
        }


    }

    /**
     * 用户密码加密
     * @param passWord 原密码
     * @return 加密之后的密码
     */
    public String passWordEncryption(String passWord){
        return createJWT(passWord, null);
    }

    /**
     * 创建用户token
     * @param param 需要封装的参数的String类型
     * @return 生成的用户token
     */
    public String createToken(String param){
        return createJWT(param, "create");
    }

}