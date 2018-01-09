package com.itheima.bos.service.base;

import com.itheima.bos.domain.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StandarService {
    void save(Standard model);

    Page<Standard> pageQuery(Pageable pageable);

    List<Standard> findAll();
}
