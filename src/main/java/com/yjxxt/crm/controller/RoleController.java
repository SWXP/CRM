package com.yjxxt.crm.controller;

import com.yjxxt.crm.annotations.RequirePermission;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;

    @RequirePermission(code = "602001")
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> showAllRole(Integer userId){
        return roleService.queryAllRoles(userId);
    }

    @RequirePermission(code = "6020")
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    @RequirePermission(code = "602002")
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> showRoleByParams(RoleQuery roleQuery){
        return roleService.selectByParams(roleQuery);
    }

    @RequirePermission(code = "602004")
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRoleById(Integer roleId){
        roleService.deleteRole(roleId);
        return success("删除角色成功!");
    }
    @RequestMapping("addOrUpdateRolePage")
    public String addUserPage(Integer roleId, Model model){

        if(null !=roleId){
            System.out.println(roleService.selectByPrimaryKey(roleId));
            model.addAttribute("role",roleService.selectByPrimaryKey(roleId));
        }
    return "role/add_update";
    }

    @RequirePermission(code = "602001")
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("添加用户成功!");
    }
    @RequirePermission(code = "602003")
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("更新用户成功!");
    }

    @RequestMapping("toGrantPage")
    public String toGrantPage(Integer roleId ,Model model){

        if(null !=roleId){
            model.addAttribute("roleId",roleId);
        }
        return "role/grant";
    }
    @RequirePermission(code = "602001")
    @RequestMapping("addGrand")
    @ResponseBody
    public ResultInfo addGrand(Integer roleId,Integer [] mids){
        roleService.addGrand(roleId,mids);
        return success("添加权限成功");
    }
}
