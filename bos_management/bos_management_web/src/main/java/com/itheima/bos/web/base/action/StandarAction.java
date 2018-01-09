package com.itheima.bos.web.base.action;

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.StandarService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Namespace("/")
@Scope("prototype")
@Controller
@ParentPackage("struts-default")
public class StandarAction extends ActionSupport implements ModelDriven<Standard> {

    private Standard model = new Standard();
    @Override
    public Standard getModel() {
        return model;
    }

    @Autowired
    private StandarService standarService;
    @Action(value = "standardAction_save",results = {@Result(name = "success",location = "/pages/base/standard.html",type = "redirect")})
    public String save(){

        standarService.save(model);
        return  SUCCESS;
    }
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action(value = "standarAction_pageQuery")
    public String pageQuery() throws IOException {
        Pageable pageable = new PageRequest(page-1,rows);
        Page<Standard> page = standarService.pageQuery(pageable);
        long totalElements = page.getTotalElements();
        List<Standard> content = page.getContent();

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",totalElements);
        map.put("rows",content);

        String json = JSONObject.fromObject(map).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        return NONE;
    }

    @Action(value = "standard_findAll")
    public String findAll() throws IOException {
        List<Standard> list = standarService.findAll();

        JSONArray jsonArray = JSONArray.fromObject(list);
        String json = jsonArray.toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);

        return NONE;
    }
}
