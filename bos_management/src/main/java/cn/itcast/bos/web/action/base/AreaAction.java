package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller//控制层
@Scope("prototype")//多例
@Namespace("/")//访问空间
@ParentPackage("json-default")//继承的类
public class AreaAction extends ActionSupport implements ModelDriven<Area> {
    //模型驱动
    private Area area = new Area();
    @Override
    public Area getModel() {
        return area;
    }
    @Action(value = "area_batchImport")
    public  String batchImport(){
         return NONE ;
    }

}
