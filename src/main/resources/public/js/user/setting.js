layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    form.on("submit(saveBtn)",function (obj) {
        $.ajax({
            type:"post",
            url:ctx+"/user/update",
            data:obj.field,
            dataTypedataType:"json",
            success:function (data) {
                // 判断是否成功
                if (data.code == 200) {
                    // 修改成功后，⽤户⾃动退出系统
                    layer.msg("⽤户信息修改成功!", function
                        () {
                        // 跳转到登录⻚⾯ (⽗窗⼝跳转)
                        window.parent.location.href = ctx + "/main";
                    });
                } else {
                    layer.msg(data.msg);
                }
            }
        });




        return false;





    });


});

