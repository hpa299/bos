package com.itheima.bos.service.base;

import com.itheima.bos.domain.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AreaService {
    void save(List<Area> list);
    Page<Area> pageQuery(Pageable pageable);

    List<Area> findAll();

    List<Area> findByQ(String q);
}
