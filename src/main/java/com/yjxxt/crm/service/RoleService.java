package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Permission;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.mapper.ModulesMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import com.yjxxt.crm.mapper.RoleMapper;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.utils.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModulesMapper modulesMapper;

    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }


    public Map<String,Object> selectByParams(RoleQuery roleQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        PageInfo<Role> pageInfo = new PageInfo<>(roleMapper.selectByParams(roleQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 删除角色,还未增加资源的删除
     * @param roleId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId){
        AssertUtil.isTrue(roleId==null,"角色Id未成功获取!");
        AssertUtil.isTrue(roleMapper.selectByPrimaryKey(roleId)==null,"角色不存在!");
        //原始是否有权限
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        if (count > 0) {
            //删除原来的权限
            AssertUtil.isTrue(!permissionMapper.deletePermissionByRoleId(roleId).equals(count), "取消授权失败");
        }
        AssertUtil.isTrue(roleMapper.deleteById(roleId)<1,"删除角色失败!");
    }

    /**
     * 添加角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        checkParams(role.getRoleName(),role.getRoleRemark());
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp!=null,"该角色已存在!");
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        AssertUtil.isTrue(roleMapper.insertReturnId(role)<1,"添加角色失败!");
    }


    private void checkParams(String roleName,String roleRemark){
        AssertUtil.isTrue((roleName==null||"".equals(roleName.trim()))&&(roleRemark==null||"".equals(roleRemark.trim())),"角色名和角色描述不能为空!");
        AssertUtil.isTrue(roleName==null||"".equals(roleName.trim()),"角色名不能为空!");
        AssertUtil.isTrue(roleRemark==null||"".equals(roleRemark.trim()),"角色描述不能为空!");
    }

    /**
     * 更新角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        checkParams(role.getRoleName(),role.getRoleRemark());
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        if(temp!=null) {
            AssertUtil.isTrue(!role.getId().equals(temp.getId()), "角色名已存在");
            AssertUtil.isTrue(role.getRoleRemark().equals(temp.getRoleRemark())&&role.getRoleName().equals(temp.getRoleName()),"角色信息未发生变化!");
        }
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"更新角色信息失败!");
    }


    /**
     * 添加权限
     * @param
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrand(Integer roleId,Integer [] mids) {
        //roleId非空
        Role temp = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(temp == null, "待授权的角色不存在");
        //原始是否有权限
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        if (count > 0) {
            //删除原来的权限，重新添加权限，核心表，t_role,t_permission
            AssertUtil.isTrue(!permissionMapper.deletePermissionByRoleId(roleId).equals(count), "授权失败");
        }
        //授权
        if (mids != null && mids.length > 0) {
            List<Permission> plist = new ArrayList<Permission>();
            //遍历
            for (Integer mid : mids) {
                //实例化permission
                Permission permission = new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mid);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setAclValue(modulesMapper.selectByPrimaryKey(mid).getOptValue());
                //添加集合对象
                plist.add(permission);
            }
            //批量授权
            AssertUtil.isTrue(permissionMapper.insertBatch(plist) != plist.size(), "批量授权失败了");
        }
    }
}
