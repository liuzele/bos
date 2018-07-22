package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller//控制层
@Scope("prototype")//多例
@ParentPackage("json-default")
@Namespace("/")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {
    private Courier courier = new Courier();//注入Courier

    @Override
    public Courier getModel() {
        return courier;
    }

    //注入Service对象
    @Autowired
    private CourierService courierService;

    //增加快递员的代码
    @Action(value = "courier_save", results = {@Result(name = "success", type = "redirect", location = "./pages/base/courier.html")})
    public String save() {
        courierService.save(courier);
        return SUCCESS;

    }

    //属性注入
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    //从数据库中读取快递员的信息展示到页面上
    @Action(value = "courier_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        //调用业务层查询数据结果
        Pageable pageable = new PageRequest(page - 1, rows);
        // 封装条件查询对象 Specification
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            // Root 用于获取属性字段，CriteriaQuery可以用于简单条件查询，CriteriaBuilder 用于构造复杂条件查询
            public Predicate toPredicate(Root<Courier> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                // 简单单表查询
                if (StringUtils.isNotBlank(courier.getCourierNum())) {
                    Predicate p1 = cb.equal(
                            root.get("courierNum").as(String.class),

                            courier.getCourierNum());

                    list.add(p1);
                }
                if (StringUtils.isNotBlank(courier.getCompany())) {
                    Predicate p2 = cb.like(
                            root.get("company").as(String.class),
                            "%" + courier.getCompany() + "%");
                    list.add(p2);
                }
                if (StringUtils.isNotBlank(courier.getType())) {
                    Predicate p3 = cb.equal(root.get("type").as(String.class),
                            courier.getType());
                    list.add(p3);
                }
                // 多表查询
                Join<Courier, Standard> standardJoin = root.join("standard",
                        JoinType.INNER);
                if (courier.getStandard() != null
                        && StringUtils.isNotBlank(courier.getStandard()
                        .getName())) {
                    Predicate p4 = cb.like(
                            standardJoin.get("name").as(String.class), "%"
                                    + courier.getStandard().getName() + "%");
                    list.add(p4);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };

        // 调用业务层 ，返回 Page
        Page<Courier> pageData = courierService.findPageData(specification,
                pageable);
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());

        // 将map转换为json数据返回 ，使用struts2-json-plugin 插件
        ActionContext.getContext().getValueStack().push(result);

        return SUCCESS;
    }
    //属性驱动,,接受从前台传递过来的ids参数
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    //作废快递员的代码实现
    @Action(value="courier_delBatch",results = { @Result(name = "success", location = "./pages/base/courier.html", type = "redirect") })
    public  String delBatch(){
        //按照逗号分割字符串
        String[] idArray = ids.split(",");
        //调用业务层,批量处理作废
        courierService.delBatch(idArray);

        return SUCCESS;
    }

    //快递员批量还原的方法
    @Action(value="courier_resBatch",results = {@Result(name = "success",type = "redirect",location = "./pages/base/courier.html")})
    public String resBatch(){
        //按照逗号分割字符串
        String[] idArray = ids.split(",");
        //调用业务层,批量处理作废
        courierService.resBatch(idArray);
        return SUCCESS;
    }

}
