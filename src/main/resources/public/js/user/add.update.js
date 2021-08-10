layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        formSelects = layui.formSelects;



    /**
     * 添加或更新用户
     */

    form.on("submit(addOrUpdateUser)", function (data){
        // 弹出loading层
        var index = top.layer.msg('数据提交中，请稍候',
            {icon: 16,
                time: false,
                shade: 0.8});
        var url = ctx+"/user/save";
        if($("input[name='id']").val()){
            url = ctx+"/user/update";
        }
        $.post(url,data.field,function (result) {
            if(result.code == 200){
                setTimeout(function () {
// 关闭弹出层（返回值为index的弹出层）
                    top.layer.close(index);
                    top.layer.msg(result.msg,{icon: 6});
// 关闭所有ifream层
                    layer.closeAll("iframe");
// 刷新⽗⻚⾯
                    parent.location.reload();
                }, 500);
            }else {
                layer.msg(result.msg,{icon: 5});
            }
        });
        return false;
    });






    /**
     * 关闭弹出层
     */

    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    })





    /**
     * 加载下拉框数据
     */
    var userId = $("input[name='id']").val();
    formSelects.config('selectId',{
        type:"post",
        searchUrl:ctx + "/role/queryAllRoles?userId="+userId,
//⾃定义返回数据中name的key, 默认 name
        keyName: 'roleName',
//⾃定义返回数据中value的key, 默认 value
        keyVal: 'id'
    },true);






})