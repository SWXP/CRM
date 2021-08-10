package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModulesController extends BaseController {
    @Resource
    private ModuleService moduleService;


    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeDto> queryAllModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }
    @RequestMapping("index")
    public String index(){
        return "module/module";
    }
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> modulesList(){
        return moduleService.modulesList();
    }
}
