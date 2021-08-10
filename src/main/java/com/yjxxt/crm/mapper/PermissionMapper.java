package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    List<Integer> queryRoleHasAllModuleIdsByRoleId(Integer roleId);

    Integer countPermissionByRoleId(Integer roleId);

    Integer deletePermissionByRoleId(Integer roleId);

    List<String> queryUserHasRolesHasAclsByUserId(Integer userId);
}