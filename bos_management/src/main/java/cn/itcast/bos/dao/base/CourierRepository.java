package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CourierRepository extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {
    //批量作废快递员
    @Query(value = "update Courier set deltag='0' where id = ?")
    @Modifying
    void updateDelTag(Integer id);
    //批量还原快递员
    @Query(value = "update Courier set deltag='1' where id = ?")
    @Modifying
    void restoreDelTag(Integer id);
}
