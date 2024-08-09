package com.vincenttho.jpa.demo.model;


/**
 * <p>订单查询条件</p>
 *
 * @author VincentHo
 * @date 2024-08-01
 */
public class OrderQueryCondition {

    private String pid;

    private String orderItemName;

    private Long orderNo;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOrderItemName() {
        return orderItemName;
    }

    public void setOrderItemName(String orderItemName) {
        this.orderItemName = orderItemName;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }
}
