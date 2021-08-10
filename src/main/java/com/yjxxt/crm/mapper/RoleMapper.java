package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Role;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    @MapKey("")
    List<Map<String,Object>> queryAllRoles(Integer userId);

    @MapKey("")
    List<Map<String,Object>> selectByParams();

    int deleteById(Integer id);

    Role selectByRoleName(String roleName);

    Integer insertReturnId(Role role);
}