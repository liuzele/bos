package cn.itcast.bos.service.base;


import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;



/**
 * 快递员的操作接口
 */
public interface CourierService {

    public void save(Courier courier);

    Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable);

    void delBatch(String[] idArray);

    void resBatch(String[] idArray);
}
