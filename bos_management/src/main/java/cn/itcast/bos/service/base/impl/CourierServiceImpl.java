package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class CourierServiceImpl implements CourierService{
    @Autowired
    private CourierRepository courierRepository;
    @Override
    public void save(Courier courier) {
        courierRepository.save(courier);
    }

    @Override
    public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable) {
        return courierRepository.findAll(specification,pageable);
    }

    //批量作废快递员的代码实现
    @Override
    public void delBatch(String[] idArray) {
        //调用dao层中的update方法,将dataid修改为0;
        for (String idStr : idArray) {
             Integer id = Integer.parseInt(idStr);//将字符串的distr转换成int类型
            //调用dao层的方法将作废修改为0;
            courierRepository.updateDelTag(id);
            
        }
    }
    //批量还原快递员的代码实现
    @Override
    public void resBatch(String[] idArray) {
        for (String idStr : idArray) {
            Integer id = Integer.parseInt(idStr);//将字符串的distr转换成int类型
            //调用dao层的方法将作废修改为0;
            courierRepository.restoreDelTag(id);

        }

    }


}
