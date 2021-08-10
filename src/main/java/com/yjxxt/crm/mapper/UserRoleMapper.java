package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.UserRole;

import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    int countUserRoleByUserId (Integer userId);

    int deleteUserRoleByUserId(Integer userId);

    int selectByUserIds(Integer[] userId);

    List<String> selectRoleIdsByUserId(Integer userId);
}