layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /*
    用户列表展示
     */

    var tableIns = table.render({
        elem: '#userList',
        url : ctx+'/user/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'userName', title: '⽤户名', minWidth:50, align:"center"},
            {field: 'email', title: '⽤户邮箱', minWidth:100, align:'center'},
            {field: 'phone', title: '⽤户电话', minWidth:100, align:'center'},
            {field: 'trueName', title: '真实姓名', align:'center'},
            {field: 'createDate', title: '创建时间',
                align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间',
                align:'center',minWidth:150},
            {title: '操作', minWidth:150,
                templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });


    $(".search_btn").on("click",function () {
        tableIns.reload({
           page: {
               curr: 1
           },
            where:{
                userName: $("input[name='userName']").val(), //⽤户名
                email: $("input[name='email']").val(), //邮箱
                phone: $("input[name='phone']").val() //⼿机号
            }
        })
    });

    /**
     * 头部⼯具栏事件
     */
    table.on("toolbar(users)",function (obj) {
        var checkStatus =  table.checkStatus(obj.config.id);
        if(obj.event === 'add'){
            //添加
            openAddOrUpdateUserDialog();
        }else if(obj.event === 'del'){
            //删除
            deleteUserByIds(checkStatus.data);
        }
    })


    /*
    打开用户管理界面
     */
    function openAddOrUpdateUserDialog(userId){
        var url = ctx+"/user/addOrUpdateUserPage";
        var title = "<h2>用户管理-添加</h2>";
        if(userId){
            url += "?id="+userId;
            title = "<h2>用户管理-更新</h2>";
        }

        layui.layer.open({
            content : url,
            title : title,
            type : 2,
            area:["650px","400px"],
            maxmin:true
        });

    }

    /*
    用户删除提示窗
     */
    function deleteUserByIds(datas){
        if(datas==null||datas.length===0){
            layer.msg("请选择要删除的用户!");
            return;
        }

        var ids =[];
        for(var i in datas){
            ids.push(datas[i].id);
        }


        layer.confirm("此操作不可恢复,确定删除用户?", {
            btn: ["确认", "取消"]
        }, function (index) {
            //关闭
            layer.close(index);
            //批量删除
            $.post(ctx + "/user/delete", {"ids": ids.toString()}, function (data) {
                if (data.code == 200) {
                    layer.msg(data.msg, {icon: 6});
                    //重载加载数据
                    tableIns.reload()
                } else {
                    //删除失败的提示信息
                    layer.msg(data.msg, {icon: 5});
                }
            }, "json");
        });
    }



    /**
     * ⾏监听事件
     */
    table.on("tool(users)", function(obj){
        var layEvent = obj.event;
// 监听编辑事件
        if(layEvent === "edit") {
            openAddOrUpdateUserDialog(obj.data.id);
        }else if(layEvent === "del"){
            deleteUserById(obj.data.id);
        }
    });




    //单独删除一条记录
    function deleteUserById(id) {
        layer.confirm("你确定要是删除数据吗?", {
            btn: ["确认", "取消"]
        }, function (index) {
            //关闭
            layer.close(index);
            //批量删除
            $.post(ctx + "/user/delete", {"ids": id}, function (data) {
                if (data.code == 200) {
                    layer.msg(data.msg, {icon: 6});
                    //重载加载数据
                    tableIns.reload()
                } else {
                    //删除失败的提示信息
                    layer.msg(data.msg, {icon: 5});
                }
            }, "json");
        });
    }











});