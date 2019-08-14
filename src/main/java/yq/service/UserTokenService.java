package yq.service;

import yq.entity.UserToken;

public interface UserTokenService {

    UserToken getUserTokenByToken(String token);

    UserToken updateAndSaveUserToken(UserToken userToken);

}
