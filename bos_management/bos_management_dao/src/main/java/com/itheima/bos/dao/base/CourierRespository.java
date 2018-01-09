package com.itheima.bos.dao.base;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CourierRespository extends JpaRepository<Courier,Long> {
    @Modifying
    @Query("update Courier set deltag = 1 where id = ?") // JPQL = HQL
    void updateDelTag(Long id);
}
