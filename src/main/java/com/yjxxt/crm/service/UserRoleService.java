package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.UserRole;
import com.yjxxt.crm.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserRoleService extends BaseService<UserRole,Integer> {
    @Resource
    private UserRoleMapper userRoleMapper;

    public List<String> selectRoleIdsByUserId(Integer userId){
        return userRoleMapper.selectRoleIdsByUserId(userId);
    }
}
