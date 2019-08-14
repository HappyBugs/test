package yq.mapper;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yq.entity.Test;

@Repository
public interface MySqlMapper extends JpaRepository<Test,Long> {

    Test findByPhone(@Param("phone")String phone);

}
