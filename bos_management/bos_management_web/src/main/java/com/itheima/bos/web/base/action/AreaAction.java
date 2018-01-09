package com.itheima.bos.web.base.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.AreaService;
import com.itheima.bos.web.common.CommonAction;
import com.itheima.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * ClassName:AreaAction <br/>
 * Function: <br/>
 * Date: Nov 30, 2017 10:22:16 AM <br/>
 */

@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class AreaAction extends CommonAction<Area> {

    public AreaAction() {
        super(Area.class);
    }

    private static final long serialVersionUID = -1686667406960026356L;

    // 使用属性驱动获取用户上传的文件
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    @Autowired
    private AreaService areaService;

    // 导入Excel
    @Action(value = "areaAction_importXSL", results = {@Result(name = "success",
            location = "/pages/base/area.html", type = "redirect")})
    public String importXLS() {
        try {

            // 用于存放Area的集合
            // 如果一条一条数据的插入数据库性能太低,所以使用一个集合,一次性进行插入
            List<Area> list = new ArrayList<>();

            // 加载文件
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));

            // 读取第一个工作簿的内容
            HSSFSheet sheet = workbook.getSheetAt(0);
            // 遍历所有的行
            for (Row row : sheet) {
                // 跳过第一行
                if (row.getRowNum() == 0) {
                    continue;
                }

                String province = row.getCell(1).getStringCellValue();
                String city = row.getCell(2).getStringCellValue();
                String district = row.getCell(3).getStringCellValue();
                String postcode = row.getCell(4).getStringCellValue();
                // 截掉省市区的最后一个字符
                province = province.substring(0, province.length() - 1);
                city = city.substring(0, city.length() - 1);
                district = district.substring(0, district.length() - 1);
                // 生成城市编码
                String citycode =
                        PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();
                // 生成简码
                String[] headByString = PinYin4jUtils
                        .getHeadByString(province + city + district);
                String shortcode =
                        PinYin4jUtils.stringArrayToString(headByString);

                Area area = new Area();
                area.setProvince(province);
                area.setCity(city);
                area.setDistrict(district);
                area.setPostcode(postcode);
                area.setCitycode(citycode);
                area.setShortcode(shortcode);

                list.add(area);

            }

            areaService.save(list);
            // 释放资源
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return SUCCESS;
    }

    // 分页查询,EasyUI请求的方式是AJAX,返回的数据应该是JSON格式的
    @Action("areaAction_pageQuery")
    public String pageQuery() throws IOException {
        // EasyUI的页码从1开始,SpringDataJPA框架构造PageRequest的时候,page是从0开始的
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Area> page = areaService.pageQuery(pageable);

        page2Json(page, new String[] {"subareas"});

        return NONE;
    }

    // 使用属性驱动获取用户输入的内容
    private String q;

    public void setQ(String q) {
        this.q = q;
    }

    // 查询所有的区域,提供给添加分区的页面使用
    @Action("areaAction_findAll")
    public String findAll() throws IOException {
        List<Area> list = null;

        if (StringUtils.isEmpty(q)) {
            list = areaService.findAll();
        } else {
            list = areaService.findByQ(q);
        }

        // 生成的JSON字符串必须要包含name字段
        // 要知道对应的JSON库是如何生成JSON字符串
        // 我们现在正在使用的JSON库生成JSON字符串使用的方法是调用JavaBean中的getter方法
        list2Json(list, new String[] {"subareas"});

        return NONE;
    }

}
