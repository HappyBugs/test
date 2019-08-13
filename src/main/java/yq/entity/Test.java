package yq.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
public class Test {

    @Id
    private Long id;                //数据库主键
    private String phone;           //电话号码
    private String passWord;        //密码
    private String userName;        //用户名
    private String role;            //角色
    private String turisdiction;    //权限  使用英文 , 隔开

}
