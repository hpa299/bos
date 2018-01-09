package com.itheima.bos.service.base.impl;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {
    @Autowired
    private CourierRepository courierRepository;
    @Override
    public void save(Courier model) {
        courierRepository.save(model);
    }

    @Override
    public Page<Courier> pageQuery(Specification specification,Pageable pageable) {
        return courierRepository.findAll(pageable);
    }

    @Override
    public void batchDel(String ids) {
        if (StringUtils.isNotEmpty(ids)){
            String[] split = ids.split(",");
            for (String id: split
                 ) {
                courierRepository.updateDelTag(Long.parseLong(id));
            }
        }
    }
}
