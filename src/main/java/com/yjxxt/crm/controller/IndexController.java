package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.service.PermissionService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }


    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //获得cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //根据userId在数据库中查询user
        User user = userService.selectByPrimaryKey(userId);
        request.setAttribute("user",user);

        List<String> permissions = permissionService.queryUserHasRolesHasAclsByUserId(userId);

        request.getSession().setAttribute("permissions",permissions);
        return "main";
    }
}
