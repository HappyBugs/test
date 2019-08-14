package yq.service;


import yq.entity.Test;

public interface MySqlService {

    /**
     * 根据id获得test对象
     * @param id
     * @return
     */
    Test getTestById(Long id);

    void addTest(Test test);

    Test getTestByPhone(String phone);

}
