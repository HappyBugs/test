//package yq.SpringSecurity;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import yq.entity.Test;
//
//import java.util.Collection;
//import java.util.List;
//
////可以用于得到登录用户的信息 用于用户身份认证
//public class SecurityUserDetails implements UserDetails {
//
//    //这是保存的主体信息
//    private Test test;
//    //所有的权限
//    private List<GrantedAuthority> authorities;
//
//    public SecurityUserDetails(Test test,List<GrantedAuthority> authorities){
//        this.test = test;
//        this.authorities = authorities;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return test.getPassWord();
//    }
//
//    @Override
//    public String getUsername() {
//        return test.getId().toString();
//    }
//
//    public Test getTest(){
//        return test;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return false;
//    }
//}
