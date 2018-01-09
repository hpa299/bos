package com.itheima.bos.web.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

/**
 * ClassName:CommonAction <br/>
 * Function: 作为所有Action的父类<br/>
 * Date: Nov 30, 2017 11:51:35 AM <br/>
 */
public class CommonAction<T> extends ActionSupport implements ModelDriven<T> {

    private T model;
    private Class<T> clazz;

    public CommonAction(Class<T> clazz) {
        this.clazz = clazz;
        try {
            model = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public T getModel() {
        return model;
    }

    protected int page; // 当前页码
    protected int rows; // 每一页显示多少条数据

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void page2Json(Page<T> page, String[] excludes) throws IOException {
        // 总数据条数
        long totalElements = page.getTotalElements();
        // 当前页面要显示的数据
        List<T> content = page.getContent();

        Map<String, Object> map = new HashMap<>();
        map.put("total", totalElements);
        map.put("rows", content);
        // 把map集合转换成json字符串

        // 指定要忽略的字段
        JsonConfig config = new JsonConfig();
        config.setExcludes(excludes);

        // JSONObject : 把一个对象或者map集合转换成json字符串
        // JSONArray : 把数组或者list集合转换成json字符串
        String json = JSONObject.fromObject(map, config).toString();

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }

    public void list2Json(List list, String[] excludes) throws IOException {

        // 指定要忽略的字段
        JsonConfig config = new JsonConfig();
        config.setExcludes(excludes);

        String json = JSONArray.fromObject(list, config).toString();

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(json);
    }

}
