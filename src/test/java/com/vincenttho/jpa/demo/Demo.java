package com.vincenttho.jpa.demo;

import com.vincenttho.jpa.demo.model.OrderPO;
import com.vincenttho.jpa.demo.model.OrderQueryCondition;
import com.vincenttho.jpa.domain.LambdaSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Date;

/**
 * <p>调用示例</p>
 *
 * @author VincentHo
 * @date 2024-08-09
 */
public class Demo {

    /**
     * <p>调用示例</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param orderQueryCondition
     */
    public void demo(OrderQueryCondition orderQueryCondition) {
        Specification specification = LambdaSpecification.query(OrderPO.class)
                .eq(OrderPO::getOrderNo, orderQueryCondition.getOrderNo())
                .eq(true, OrderPO::getOrderItemName, orderQueryCondition.getOrderItemName())
                .andOr(LambdaSpecification.query(OrderPO.class)
                        .eq(true, OrderPO::getPid, orderQueryCondition.getPid())
                        .eq(true, OrderPO::getOrderItemName, orderQueryCondition.getOrderItemName())
                        .build()
                )
                .in(OrderPO::getPid, "1", "2", "3")
                .notIn(OrderPO::getPid, Arrays.asList("a", "b", "c"))
                .gt(OrderPO::getCreateDate, new Date())
                .eq(OrderPO::getOrderNo, OrderPO::getOrderNo)
                .build();
    }

}
