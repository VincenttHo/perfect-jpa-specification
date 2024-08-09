package com.vincenttho.jpa.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ipn_order")
public class OrderPO {
    private static final long serialVersionUID = 1L;

    /** 主键*/
    @Id
    private String pid;

    /** 医嘱流水号  seq_emr_order_no 序列号 */
    private Long orderNo;

    /** 医嘱项目名称 */
    private String orderItemName;

    private Date createDate;

    public String getPid() {
        return pid;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public String getOrderItemName() {
        return orderItemName;
    }

    public Date getCreateDate() {
        return createDate;
    }
}