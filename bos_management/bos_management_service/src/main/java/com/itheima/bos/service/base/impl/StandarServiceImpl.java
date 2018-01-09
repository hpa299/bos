package com.itheima.bos.service.base.impl;

import com.itheima.bos.dao.base.StandardRepository;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.StandarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StandarServiceImpl implements StandarService {
    @Autowired
    private StandardRepository standardRepository;

    @Override
    public void save(Standard model) {
        standardRepository.save(model);
    }

    @Override
    public Page<Standard> pageQuery(Pageable pageable) {
        return standardRepository.findAll(pageable);
    }

    @Override
    public List<Standard> findAll() {
        return standardRepository.findAll();
    }
}
