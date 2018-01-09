package com.itheima.bos.dao.base;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AreaRespository extends JpaRepository<Area,Long> {
    // 根据用户在输入框中输入的内容,模糊匹配区域
    @Query("from Area where province like ?1 or city like ?1 or  district like ?1 or postcode like ?1 or citycode like ?1 or shortcode like ?1")
    List<Area> findByQ(String q);
}
