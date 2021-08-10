layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //角色列表展示
    var tableIns = table.render({
        elem: '#roleList',
        url: ctx + '/role/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "roleListTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: "id", title: '编号', fixed: "true", width: 80},
            {field: 'roleName', title: '角色名', minWidth: 50, align: "center"},
            {field: 'roleRemark', title: '角色备注', minWidth: 100, align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center', minWidth: 150},
            {field: 'updateDate', title: '更新时间', align: 'center', minWidth: 150},
            {title: '操作', minWidth: 150, templet: '#roleListBar', fixed: "right", align: "center"}
        ]]
    });

    // 多条件搜索
    $(".search_btn").on("click", function () {
        table.reload("roleListTable", {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                roleName: $("input[name='roleName']").val()
            }
        })
    });


    /*绑定头部工具栏*/

    table.on("toolbar(roles)", function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        if (obj.event === 'add') {

            openAddOrUpdateRoleDialog(checkStatus.data.id);

        } else if (obj.event === 'grant') {
            openAddGrantPage(checkStatus.data);
        }

    });


    function openAddOrUpdateRoleDialog(roleId) {
        var title = "<h3>角色模块--新增</h3>";
        var url = ctx + "/role/addOrUpdateRolePage";
        if (roleId) {
            title = "<h3>角色模块--更新</h3>";
            url += "?roleId=" + roleId;
        }

        //打开iframe层
        layui.layer.open({
            type: 2,
            title: title,
            content: url,
            area: ["600px", "280px"],
            maxmin: true
        })
    }

    function openAddGrantPage(datas){
        if(datas.length==0){
            layer.msg("请选择授权的角色",{icon:5});
            return ;
        }

        if(datas.length>1){
            layer.msg("暂时不支持批量授权",{icon:5});
            return ;
        }

        var title="角色授权--授权";
        var url=ctx+"/role/toGrantPage?roleId="+datas[0].id;
        //iframe
        layer.open({
            type:2,
            title:title,
            content: url,
            area: ["600px","280px"],
            maxmin:true
        })
    }



    /*绑定行内工具栏*/
    table.on("tool(roles)", function (obj) {
        if (obj.event === 'edit') {
            openAddOrUpdateRoleDialog(obj.data.id);
        } else if (obj.event === 'del') {
            deleteRoleById(obj.data.id);
        }
    });


    function deleteRoleById(roleId) {
        layer.confirm("你确定要删除数据吗?", {
            btn: ["确定", "取消"],
        }, function (index) {
            //关闭
            top.layer.close(index);
            //ajax删除
            $.post(ctx + "/role/delete", {"roleId": roleId}, function (data) {
                //判断
                if (data.code == 200) {
                    layer.msg("删除成功了", {icon: 6});
                    //重新加载数据
                    tableIns.reload();
                } else {
                    layer.msg(data.msg, {icon: 5});
                }
            }, "json");
        });
    }
});