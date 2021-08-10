package com.yjxxt.crm.controller;

import com.yjxxt.crm.annotations.RequirePermission;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.mapper.UserRoleMapper;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserRoleService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private UserRoleService userRoleService;

    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(HttpServletRequest request,String userName,String userPwd){
        //创建一个新的ResultInfo对象
        ResultInfo resultInfo = success();
//        try{
            //进行登陆验证,若登陆成功返回UserModel对象
            UserModel userModel = userService.userLogin(userName,userPwd);
            //将UserModel对象存入ResultInfo对象中
            resultInfo.setResult(userModel);
/*        }catch (ParamsException ps){
            //捕捉异常,设置对应的信息
            ps.printStackTrace();
            resultInfo.setCode(ps.getCode());
            resultInfo.setMsg(ps.getMsg());
        }catch (Exception e){
            //捕捉异常,设置对应的信息
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("登陆失败!");
        }*/
        //返回结果信息
        return  resultInfo;
    }


    @PostMapping("updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,String oldPassword,String newPassword,String confirmPassword){
        //创建一个新的ResultInfo对象
        ResultInfo resultInfo = success();
//        try{
            //从cookie中获取userId
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);

            userService.updateUserPassword(userId,oldPassword,newPassword,confirmPassword);
/*        }catch (ParamsException ps){
            ps.printStackTrace();
            resultInfo.setCode(ps.getCode());
            resultInfo.setMsg(ps.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("修改失败!");
        }*/

        return resultInfo;
    }

    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }



    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }


    @RequirePermission(code = "601002")
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> showUserByParams(UserQuery userQuery){
        return userService.selectByParams(userQuery);
    }

    @RequirePermission(code = "6010")
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    @RequirePermission(code = "601001")
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("添加用户成功!");
    }
    //在这里加此注解会导致用户无法修改自己的资料
    //@RequirePermission(code = "601003")
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("更新用户信息成功!");
    }
    @RequestMapping("addOrUpdateUserPage")
    public String addOrUpdateUserPage(Integer id , Model model){
        if(id !=null){
            model.addAttribute("user",userService.selectByPrimaryKey(id));
        }

        return "user/add_update";
    }

    @RequestMapping("toSettingPage")
    public String toSettingPage(HttpServletRequest request,Model model){
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(userId);
        //保证修改用户信息时,带着用户的角色
        List<String> listRoleIds = userRoleService.selectRoleIdsByUserId(userId);
        String roleIds = StringUtils.join(listRoleIds.toArray(),",");
        System.out.println(roleIds);
        user.setRoleIds(roleIds);
        model.addAttribute("user",user);
        return "user/setting";
    }

    @RequirePermission(code = "601004")
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUser(ids);
        return success("删除成功!");
    }
}
