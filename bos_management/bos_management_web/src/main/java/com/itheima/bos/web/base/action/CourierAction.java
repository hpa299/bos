package com.itheima.bos.web.base.action;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {
    private Courier model = new Courier();
    @Override
    public Courier getModel() {
        return model;
    }
    @Autowired
    private CourierService courierService;

    // 保存快递员
    @Action(value = "courierAction_save", results = {@Result(name = "success",
            location = "/pages/base/courier.html", type = "redirect")})
    public String save() {
        courierService.save(model);
        return SUCCESS;
    }
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action(value = "courierAction_pageQuery")
    public String pageQuery() throws IOException {
        // 动态的构造查询条件
        Specification<Courier> specification = new Specification<Courier>() {

            // 在这个方法中构造查询条件
            // root : 根对象,一般可以直接理解为泛型对象.以本例来说,root可以简单的理解为就是Courier对象
            // cb : 用来构造查询条件的对象
            @Override
            public Predicate toPredicate(Root<Courier> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {

                // 用来装查询条件的一个集合
                List<Predicate> list = new ArrayList<>();

                String courierNum = getModel().getCourierNum();
                String company = getModel().getCompany();
                String type = getModel().getType();
                Standard standard = getModel().getStandard();

                // 如果快递员编号不为空,构造一个等值查询
                if (StringUtils.isNotEmpty(courierNum)) {
                    // 第二个参数,要比较的值,可以理解为执行查询的时候要传入的具体的值
                    Predicate p1 =
                            cb.equal(root.get("courierNum").as(String.class),
                                    courierNum);
                    list.add(p1);
                }
                // 如果公司不为空,构造一个模糊查询
                if (StringUtils.isNotEmpty(company)) {
                    // 第二个参数,要比较的值,可以理解为执行查询的时候要传入的具体的值
                    Predicate p2 = cb.like(root.get("company").as(String.class),
                            "%" + company + "%");
                    list.add(p2);
                }
                // 如果快递员类型不为空,构造一个等值查询
                if (StringUtils.isNotEmpty(type)) {
                    // 第二个参数,要比较的值,可以理解为执行查询的时候要传入的具体的值
                    Predicate p3 =
                            cb.equal(root.get("type").as(String.class), type);
                    list.add(p3);
                }

                // 派送标准不为空,构建一个等值查询
                if (standard != null) {
                    String name = standard.getName();
                    if (StringUtils.isNotEmpty(name)) {

                        Join<Object, Object> join = root.join("standard");
                        Predicate p4 = cb
                                .equal(join.get("name").as(String.class), name);
                        list.add(p4);
                    }

                }

                // 如果用户没有输入任何查询条件,返回null
                if (list.size() == 0) {
                    return null;
                }

                // 构建多个and条件的时候,需要使用数组
                // 所以要把list集合转为数组
                Predicate[] arr = new Predicate[list.size()];
                list.toArray(arr);
                // 构造and查询条件
                return cb.and(arr);
            }
        };
        Pageable pageable = new PageRequest(page-1,rows);
        Page<Courier> page = courierService.pageQuery(specification,pageable);
        long totalElements = page.getTotalElements();
        List<Courier> content = page.getContent();

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",totalElements);
        map.put("rows",content);

        JsonConfig config = new JsonConfig();
        config.setExcludes(new String[] {"fixedAreas","takeTime"});
        String json = JSONObject.fromObject(map, config).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);

        return NONE;
    }

    // 使用属性驱动获取要删除的快递员的ID
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    // 删除快递员
    @Action(value = "courierAction_batchDel",
            results = {@Result(name = "success",
                    location = "/pages/base/courier.html", type = "redirect")})
    public String batchDel() {

        courierService.batchDel(ids);
        return SUCCESS;
    }
}
