layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on("submit(saveBtn)",function (data) {
        //获取表单的元素(旧密码+新密码+确认密码)
        var fieldData = data.field;
        if((fieldData.old_password=="undefined" || fieldData.old_password.trim=="")&&(fieldData.new_password=="undefined" || fieldData.new_password.trim=="")&&(fieldData.again_password=="undefined" || fieldData.again_password.trim=="")){
            layer.msg("旧密码,新密码,确认密码不能为空!");
        }
        if((fieldData.old_password=="undefined" || fieldData.old_password.trim=="")&&(fieldData.new_password=="undefined" || fieldData.new_password.trim=="")){
            layer.msg("旧密码,新密码不能为空!");
        }
        if((fieldData.new_password=="undefined" || fieldData.new_password.trim=="")&&(fieldData.again_password=="undefined" || fieldData.again_password.trim=="")){
            layer.msg("新密码,确认密码不能为空!");
        }
        if((fieldData.old_password=="undefined" || fieldData.old_password.trim=="")&&(fieldData.again_password=="undefined" || fieldData.again_password.trim=="")){
            layer.msg("旧密码,确认密码不能为空!");
        }
        if(fieldData.old_password=="undefined" || fieldData.old_password.trim==""){
            layer.msg("旧密码不能为空!");
        }
        if(fieldData.new_password=="undefined" || fieldData.new_password.trim==""){
            layer.msg("新密码不能为空!");
        }
        if(fieldData.again_password=="undefined" || fieldData.again_password.trim==""){
            layer.msg("确认密码不能为空!");
        }

        $.ajax({
            type:"post",
            url:ctx+"/user/updatePassword",
            data:{
                oldPassword:fieldData.old_password,
                newPassword:fieldData.new_password,
                confirmPassword:fieldData.again_password
            },
            dataTypedataType:"json",
            success:function (data) {
            // 判断是否成功
                if (data.code == 200) {
            // 修改成功后，⽤户⾃动退出系统
                    layer.msg("⽤户密码修改成功，系统将在3秒钟后退出...", function
                        () {
            // 退出系统后，删除对应的cookie
                        $.removeCookie("userIdStr",
                            {domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",
                            {domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",
                            {domain:"localhost",path:"/crm"});
                        // 跳转到登录⻚⾯ (⽗窗⼝跳转)
                        window.parent.location.href = ctx + "/index";
                    });
                } else {
                    layer.msg(data.msg);
                }
            }
        });
    });

});