package com.itheima.bos.service.base.impl;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaRepository areaRepository;
    // 保存区域
    @Override
    public void save(List<Area> list) {
        areaRepository.save(list);
    }
    // 分页查询
    @Override
    public Page<Area> pageQuery(Pageable pageable) {
        return areaRepository.findAll(pageable);
    }

    // 查询所有区域
    @Override
    public List<Area> findAll() {
        return areaRepository.findAll();
    }

    // 根据用户在输入框中输入的内容,模糊匹配区域
    @Override
    public List<Area> findByQ(String q) {
        q = "%" + q.toUpperCase() + "%";
        return areaRepository.findByQ(q);
    }
}
