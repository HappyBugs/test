package yq.mapper;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import yq.entity.UserToken;

public interface UserTokenRepo extends JpaRepository<UserToken,Long> {

    UserToken findByToken(@Param("token")String token);

}
