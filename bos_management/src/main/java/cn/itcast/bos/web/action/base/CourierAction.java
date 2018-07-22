package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import java.util.HashMap;
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
    @Action(value = "courier_save", results = {@Result(name ="success",type = "redirect",location = "./pages/base/courier.html")})
    public String save(){
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
    @Action(value = "courier_pageQuery",results ={@Result(name = "success",type = "json")})
    public String pageQuery(){
        //调用业务层查询数据结果
      org.springframework.data.domain.Pageable pageable = new PageRequest(page - 1, rows);
        Page<Courier> pageData = courierService.findPageData(pageable);
        // 返回客户端数据 需要 total 和 rows
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());

        // 将map转换为json数据返回 ，使用struts2-json-plugin 插件
        ActionContext.getContext().getValueStack().push(result);

        return SUCCESS;
    }

}
