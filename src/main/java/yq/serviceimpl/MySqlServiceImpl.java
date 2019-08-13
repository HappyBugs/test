package yq.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import yq.commons.responseBase.BaseApiService;
import yq.entity.Test;
import yq.mapper.MySqlMapper;
import yq.service.MySqlService;

@Service
public class MySqlServiceImpl extends BaseApiService implements MySqlService {

    @Autowired
    private MySqlMapper mySqlMapper;

    @Override
    @Transactional
    public Test getTestById(Long id) {
        return mySqlMapper.findById(id).get();
    }

    @Override
    @Transactional
    public void addTest(Test test) {
        Assert.notNull(mySqlMapper.save(test),"创建Test失败");
    }
}
