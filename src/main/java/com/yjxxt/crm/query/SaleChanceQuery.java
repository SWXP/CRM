package com.yjxxt.crm.query;


import com.yjxxt.crm.base.BaseQuery;

/**
 * 营销机会查询条件
 * @author ZHAI
 */
public class SaleChanceQuery extends BaseQuery {
    //客户姓名
    private String customerName;
    //创建人
    private String createMan;
    //分配状态
    private String state;

    public SaleChanceQuery() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SaleChanceQuery{" +
                "customerName='" + customerName + '\'' +
                ", createMan='" + createMan + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
