package yq.Shiro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 用户身份验证的凭证
 */
@Data
//生成默认构造器
@NoArgsConstructor
//生产带所有属性的构造器
@AllArgsConstructor
public class NewAuthenticationToken implements AuthenticationToken {

    private Long id;
    private String token;

    //得到主体
    @Override
    public Object getPrincipal() {
        return this.id;
    }

    //得到凭证
    @Override
    public Object getCredentials() {
        return this.token;
    }
}
