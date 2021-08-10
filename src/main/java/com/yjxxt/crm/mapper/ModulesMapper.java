package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Modules;
import com.yjxxt.crm.dto.TreeDto;

import java.util.List;
import java.util.Map;

public interface ModulesMapper extends BaseMapper<Modules,Integer> {

    List<TreeDto> queryAllModules();

    List<Modules> queryModules();

}