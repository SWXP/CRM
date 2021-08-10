package com.yjxxt.crm.controller;

import com.yjxxt.crm.annotations.RequirePermission;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.service.SaleChanceService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Configuration
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private UserService userService;


    /**
     * 营销机会数据表单展示数据查询,以及根据条件筛选
     * @param query
     * @return
     */
    @RequirePermission(code = "101001")
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams (SaleChanceQuery query) {
        return saleChanceService.querySaleChanceByParams(query);
    }

    /**
     * 营销机会管理按钮请求地址,对请求进行跳转到营销机会表单展示页面
     * @return
     */
    @RequirePermission(code = "1010")
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    @RequirePermission(code = "101002")
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveSaleChance(HttpServletRequest request,SaleChance saleChance){
        //获取创建人Id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //获取用户的真实姓名
        String trueName = userService.selectByPrimaryKey(userId).getTrueName();
        //设置创建人
        saleChance.setCreateMan(trueName);
        //添加数据
        saleChanceService.saveSaleChance(saleChance);
        return success("营销机会数据添加成功!");
    }

    /**
     * 机会数据添加与更新表单页面视图转发
     * id为空 添加操作
     * id非空 修改操作
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer id, Model model){
        //如果发来的请求数据中包含id,那么就为修改数据
        if(id!=null){
            //根据id将对应的营销数据查询出来
            SaleChance saleChance =saleChanceService.selectByPrimaryKey(id);
            //将数据存放到作用域中,供下一个页面显示
            model.addAttribute("saleChance",saleChance);
        }

        return "saleChance/add_update";
    }

    @RequirePermission(code = "101002")
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){

        saleChanceService.updateSaleChance(saleChance);


        return success("营销机会数据更新成功!");
    }
    @RequirePermission(code = "101003")
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChances(Integer[] ids){
        saleChanceService.deleteBatch(ids);
        return success("营销机会数据删除成功!");
    }



}
