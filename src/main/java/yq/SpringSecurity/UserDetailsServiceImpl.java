//package yq.SpringSecurity;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import yq.entity.Test;
//import yq.mapper.MySqlMapper;
//import yq.service.MySqlService;
//
//import java.util.ArrayList;
//import java.util.List;
//
////用户信息验证
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private MySqlService mySqlService;
//
//    @Autowired
//    private MySqlMapper mySqlMapper;
//
//    //认证和授权
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        long id = Long.parseLong(username);
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        Test testById = mySqlService.getTestById(id);
//            if(testById == null){
//            throw new UsernameNotFoundException("用户不能为空");
//        }
//        //权限赋值
//        authorities.add(new SimpleGrantedAuthority("admin"));
//            //这里就是我们刚刚那个类
//        return new SecurityUserDetails(testById,authorities);
//    }
//}
