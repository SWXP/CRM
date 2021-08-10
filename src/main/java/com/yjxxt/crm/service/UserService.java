package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.bean.UserRole;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.mapper.UserRoleMapper;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.Md5Util;
import com.yjxxt.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.*;


/**
 * @author ZHAI
 *
 */
@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 用户登录处理
     * @param userName 前台用户名
     * @param userPwd  前台用户密码
     * @return
     */
    public UserModel userLogin(String userName,String userPwd){
        //判断账号密码是否为空
        checkLoginParams(userName,userPwd);
        //如果存在不为空,进入数据库查询
        User temp = userMapper.selectUserByUserName(userName);
        //判断是否拿到记录
        AssertUtil.isTrue(temp==null,"用户未注册或已注销!");
        //拿到记录后,判断密码是否正确
        checkLoginPwd(userPwd,temp.getUserPwd());
        //验证成功构建UserModel对象
        UserModel userModel = buildUserModel(temp);

        return userModel;
    }

    /**
     * 更新用户密码
     * @param userId 用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param confirmPassword 确认密码
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPassword){
        //根据userId在数据库中查询user
        User temp = userMapper.selectByPrimaryKey(userId);
        //判断是否拿到记录和验证密码
        checkPasswordParams(temp,oldPassword,newPassword,confirmPassword);
        //设置新密码
        temp.setUserPwd(Md5Util.encode(newPassword));
        //修改记录
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(temp)<1,"用户密码修改失败!请检查数据库状态!");

    }


    /**
     * 判断账号密码是否为空
     * 使用 org.apache.commons.lang3.StringUtils 中的isBlank方法进行判断
     * @param userName 前台用户名
     * @param userPwd  前台用户密码
     */
    private void checkLoginParams(String userName,String userPwd){
        AssertUtil.isTrue((StringUtils.isBlank(userName)&&StringUtils.isBlank(userPwd)),"用户名和密码不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空!");
    }


    /**
     * 判断密码是否正确,判断前需要将前台输入的密码使用Md5加密
     * @param userPwd 前台用户密码
     * @param dbUserPwd 数据库中用户密码
     */
    private void checkLoginPwd(String userPwd,String dbUserPwd){
        userPwd = Md5Util.encode(userPwd);
        AssertUtil.isTrue(!dbUserPwd.equals(userPwd),"用户密码错误!");
    }

    /**
     * 构建userModel
     * @param user
     * @return
     */
    private UserModel buildUserModel(User user){
        UserModel userModel = new UserModel(user);

        return userModel;
    }


    /**
     * 修改密码验证
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    private void checkPasswordParams(User user,String oldPassword,String newPassword,String confirmPassword){
        AssertUtil.isTrue(user==null,"用户未注册或已注销!");

        AssertUtil.isTrue((StringUtils.isBlank(oldPassword)&&StringUtils.isBlank(newPassword)&&StringUtils.isBlank(confirmPassword)),"旧密码,新密码,确认密码不能为空!");
        AssertUtil.isTrue((StringUtils.isBlank(oldPassword)&&StringUtils.isBlank(confirmPassword)),"旧密码和确认密码不能为空!");
        AssertUtil.isTrue((StringUtils.isBlank(newPassword)&&StringUtils.isBlank(oldPassword)),"旧密码和新密码不能为空!");
        AssertUtil.isTrue((StringUtils.isBlank(newPassword)&&StringUtils.isBlank(confirmPassword)),"新密码和确认密码不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"旧密码不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"确认密码不能为空!");
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPassword)),"旧密码不正确!");
        AssertUtil.isTrue(!newPassword.equals(confirmPassword),"新密码和确认密码不一致!");
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新旧密码不可重复!");
    }


    public List<Map<String,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }

    /**
     * 用户管理页面查询用户方法
     * @param userQuery
     * @return
     */
    public Map<String,Object> selectByParams(UserQuery userQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());

        PageInfo<User> userPageInfo = new PageInfo<>(userMapper.selectByParams(userQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("data",userPageInfo.getList());
        map.put("count",userPageInfo.getTotal());

        return map;
    }

    /**
     * 添加⽤户
     * 1. 参数校验
     * ⽤户名 ⾮空 唯⼀性
     * 邮箱 ⾮空
     * ⼿机号 ⾮空 格式合法
     * 2. 设置默认参数
     * isValid 1
     * creteDate 当前时间
     * updateDate 当前时间
     * userPwd 123456 -> md5加密
     * 3. 执⾏添加，判断结果
     */
    /**
     * 添加用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){

        checkUserParams(user.getId(),user.getUserName(), user.getEmail(), user.getPhone(),user.getRoleIds());
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));

        //使用insertHasKey可以拿到自增的主键userId
        AssertUtil.isTrue(userMapper.insertHasKey(user)<1,"用户添加失败!");
        //将userId给user_role表使用
        relationUserRole(user.getId(), user.getRoleIds());

    }

    /**
     * 将user对应角色添加到user_role表中
     * @param userId
     * @param roleIds
     */
    private void relationUserRole(Integer userId, String roleIds) {
        //统计当前用户拥有的角色
        Integer count = userRoleMapper.countUserRoleByUserId(userId);
        if(count >0){
            //删除原来的角色信息
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)<count,"角色分配过程中发生问题!");
        }

        if(StringUtils.isNotBlank(roleIds)){
            //准备user_role集合
            List<UserRole> list = new ArrayList<>();

            //将roleIds进行分割
            String[] arrayIds = roleIds.split(",");

            //遍历添加多个记录到list
            for(String roleId :arrayIds){
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());

                list.add(userRole);
            }

            AssertUtil.isTrue(userRoleMapper.insertBatch(list)!=list.size(),"角色分配失败!");
        }


    }


    private void checkUserParams(Integer id,String userName,String email,String phone,String roleIds){

        AssertUtil.isTrue((StringUtils.isBlank(userName)&&StringUtils.isBlank(email)&&StringUtils.isBlank(phone)),"用户名,邮箱,手机号码不能为空!");
        AssertUtil.isTrue((StringUtils.isBlank(userName)&&StringUtils.isBlank(phone)),"用户名和手机号码不能为空!");
        AssertUtil.isTrue((StringUtils.isBlank(email)&&StringUtils.isBlank(phone)),"邮箱和手机号码不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号码不能为空!");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号码格式不正确!");
        AssertUtil.isTrue((StringUtils.isBlank(userName)&&StringUtils.isBlank(email)),"用户名和邮箱不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空!");

        User temp = userMapper.selectUserByUserName(userName);


        System.out.println(temp);

        //用户Id不为空时,判断时更新操作
        if(id!=null) {
            AssertUtil.isTrue(!temp.getId().equals(id),"用户名已存在!");
            //System.out.println(userName+">>"+phone+">>"+email+">>"+temp.getUserName()+">>"+temp.getPhone()+">>"+temp.getEmail());
            //System.out.println(roleIds+">>>>>>>>>>>"+temp.getRoleIds());
            //由于user表中未记录roleIds所以暂时不比较
            //AssertUtil.isTrue(userName.equals(temp.getUserName())&&phone.equals(temp.getPhone())&&email.equals(temp.getEmail())&&roleIds.equals(temp.getRoleIds()),"用户信息未发生改变!");

        }else {
            AssertUtil.isTrue(temp != null, "用户已存在!");
        }
    }


    /**
     * 更新用户信息
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){

        checkUserParams(user.getId(),user.getUserName(), user.getEmail(), user.getPhone(), user.getRoleIds());
        System.out.println(user.getRoleIds());
        user.setUpdateDate(new Date());

        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"用户更新失败!");
        //将userId给user_role表使用,重新分配权限
        relationUserRole(user.getId(), user.getRoleIds());

    }

    /**
     * 删除用户和用户权限
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer[] ids){
        AssertUtil.isTrue(ids==null||ids.length==0,"请选择要删除的用户!");
        AssertUtil.isTrue(userMapper.deleteBatch(ids)<ids.length,"删除用户失败!");

        Integer count = userRoleMapper.selectByUserIds(ids);
        //批量删除权限
       AssertUtil.isTrue(userRoleMapper.deleteBatch(ids)<count,"清理对应用户权限时失败!");
/*        for (Integer id:ids) {
            Integer count = userRoleMapper.countUserRoleByUserId(id);
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(id)<count,"清理对应用户权限时失败!");
        }*/
    }

}
