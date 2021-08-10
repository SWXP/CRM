package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Modules;
import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.mapper.ModulesMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Modules,Integer> {
    @Resource
    private ModulesMapper modulesMapper;
    @Resource
    private PermissionMapper permissionMapper;



    public Map<String,Object> modulesList(){
        Map<String,Object> result = new HashMap<String,Object>();
        List<Modules> modules =modulesMapper.queryModules();
        result.put("count",modules.size());
        result.put("data",modules);
        result.put("code",0);
        result.put("msg","");
        return result;
    }

    public List<TreeDto> queryAllModules(Integer roleId){

        List<TreeDto> treeDtoList = modulesMapper.queryAllModules();

        List<Integer> roleMids=permissionMapper.queryRoleHasAllModuleIdsByRoleId(roleId);

        if(null != roleMids||roleMids.size()>0){
            for (TreeDto treeDto: treeDtoList) {
                if(roleMids.contains(treeDto.getId())){
                    treeDto.setChecked(true);
                }
            }
        }


        return treeDtoList;
    }


}
