package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.mapper.SaleChanceMapper;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    //由于layui的框架通过表格展示后端数据,除了data部分,还包括"code": ,"msg": ,"count": ,最终展示为键值对形式并符合json格式
    //所以查询结果返回Map类型,符合前端数据展示格式

    /**
     *
     * @param saleChanceQuery
     * @return
     */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));

        map.put("code",0);
        map.put("msg","");
        //拿到总记录数
        map.put("count",pageInfo.getTotal());
        //拿到分页后的记录
        map.put("data",pageInfo.getList());


        return map;
    }
/**
 * 营销机会数据添加
 * 1.参数校验
 * customerName:非空
 * linkMan:非空
 * linkPhone:非空 11位手机号
 * 2.设置相关参数默认值
 * state:默认未分配 如果选择分配人 state 为已分配
 * assignTime: 如果选择分配人 时间为当前系统时间
 * devResult:默认未开发 如果选择分配人
 devResult为开发中 0-未开发 1-开发中 2-开发成功 3-开发失败
 * isValid:默认有效数据(1-有效 0-无效)
 * createDate updateDate:默认当前系统时间
 * 3.执行添加 判断结果
 */
    /**
     * 营销机会数据添加
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSaleChance(SaleChance saleChance){
        //检验必填项是否为空
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        saleChance.setState(0);
        saleChance.setDevResult(0);

        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }

        saleChance.setIsValid(1);
        saleChance.setUpdateDate(new Date());
        saleChance.setCreateDate(new Date());

        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"营销机会数据添加失败!");

    }

    private void checkParams(String customerName,String linkMan,String linkPhone){
        AssertUtil.isTrue(StringUtils.isBlank(customerName)&&StringUtils.isBlank(linkMan)&&StringUtils.isBlank(linkPhone),"客户名称,联系人,联系电话不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(customerName)&&StringUtils.isBlank(linkMan),"客户名称,联系人不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(customerName)&&StringUtils.isBlank(linkPhone),"客户名称,联系电话不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan)&&StringUtils.isBlank(linkPhone),"联系人,联系电话不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空!");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"联系电话格式不正确!");
    }

/**
 * * 营销机会数据更新
 * * 1.参数校验
 * * id:记录必须存在
 * * customerName:非空
 * * linkMan:非空
 * * linkPhone:非空，11位手机号
 * * 2. 设置相关参数值
 * * updateDate:系统当前时间
 * * 原始记录 未分配 修改后改为已分配(由分配人决定)
 * * state 0->1
 * * assginTime 系统当前时间
 * * devResult 0-->1
 * * 原始记录 已分配 修改后 为未分配
 * * state 1-->0
 * * assignTime 待定 null
 * * devResult 1-->0
 * * 3.执行更新 判断结果
 */
    /**
     * 根据Id修改记录
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){

        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp==null,"待修改的记录不存在!");
        //检验必填项是否为空
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        if(StringUtils.isBlank(temp.getAssignMan())&&StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
       if((StringUtils.isNotBlank(temp.getAssignMan())&&StringUtils.isNotBlank(saleChance.getAssignMan()))&&!saleChance.getAssignMan().equals(temp.getAssignMan())){
            saleChance.setAssignTime(new Date());
        }




        if(StringUtils.isNotBlank(temp.getAssignMan())&&StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(0);
            saleChance.setAssignTime(null);
        }

        saleChance.setIsValid(1);
        saleChance.setUpdateDate(new Date());

        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"营销机会数据更新失败!");

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChances(Integer[] ids){
        AssertUtil.isTrue(ids==null||ids.length<1,"请选择需要删除的数据!");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)<1,"营销机会数据删除失败!");
    }

}
