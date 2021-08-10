layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    
    form.on("submit(login)",function (data) {
        //获取表单的元素(用户名+密码)
        var fieldData = data.field;


        //前端页面判断是否为空
        if((fieldData.username == "undefined" || fieldData.username.trim=="")&&(fieldData.password == "undefined" || fieldData.password.trim=="")){
            layer.msg("用户名和密码都不能为空");
            return false;
        }

        if(fieldData.username == "undefined" || fieldData.username.trim==""){
            layer.msg("用户名不能为空");
            return false;
        }
        if(fieldData.password == "undefined" || fieldData.password.trim==""){
            layer.msg("用户密码不能为空");
            return false;
        }

        //如果都通过则请求用户登录
        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                userName:fieldData.username,
                userPwd:fieldData.password
            },
            success:function (data) {
                if(data.code==200){
                    function f() {
                        var result = data.result;
                        $.cookie("userName",result.userName);
                        $.cookie("userIdStr",result.userIdStr);
                        $.cookie("trueName",result.trueName);
                        if(fieldData.rememberMe){
                            $.cookie("userName",result.userName,{expires: 7});
                            $.cookie("userIdStr",result.userIdStr,{expires: 7});
                            $.cookie("trueName",result.trueName,{expires: 7});
                        }
                        window.location.href = ctx+"/main";
                    }
                    f();
                }else{
                    // 提示信息
                    layer.msg(data.msg);
                }
            }
        });
        return false;
    });
    
});