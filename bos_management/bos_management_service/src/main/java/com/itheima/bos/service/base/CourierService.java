package com.itheima.bos.service.base;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CourierService {

    void save(Courier model);

    Page<Courier> pageQuery(Specification specification,Pageable pageable);

    void batchDel(String ids);
}
