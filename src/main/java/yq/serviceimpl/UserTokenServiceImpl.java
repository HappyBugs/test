package yq.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import yq.entity.UserToken;
import yq.mapper.UserTokenRepo;
import yq.service.UserTokenService;

public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    private UserTokenRepo userTokenRepo;


    @Override
    public UserToken getUserTokenByToken(String token) {
        UserToken byToken = userTokenRepo.findByToken(token);
        Assert.notNull(byToken,"没有该登录信息");
        return byToken;
    }

    @Override
    @Transactional
    public UserToken updateAndSaveUserToken(UserToken userToken) {
        if(userTokenRepo.existsById(userToken.getUserId())){
            UserToken myUserToken = userTokenRepo.findById(userToken.getUserId()).get();
            myUserToken.setToken(userToken.getToken());
            return userTokenRepo.save(myUserToken);
        }
        //如果表示没有该记录
        UserToken save = userTokenRepo.save(userToken);
        return save;
    }


}
