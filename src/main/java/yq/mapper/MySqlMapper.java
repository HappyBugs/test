package yq.mapper;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yq.entity.Test;

@Repository
public interface MySqlMapper extends JpaRepository<Test,Long> {

}
