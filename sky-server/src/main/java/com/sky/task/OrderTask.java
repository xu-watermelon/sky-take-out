package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    OrderMapper orderMapper;


    @Scheduled(cron="0 * * * * ?")
    public void processTimeoutOrder(){
         log.info("订单超时,自动取消:{}", LocalDateTime.now());
        //查询超时订单
       List<Orders> list= orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,LocalDateTime.now().plusMinutes(-15));
       if(list!=null&&list.size()>0){
           for (Orders orders : list) {
               //将状态改为已取消
               orders.setStatus(Orders.CANCELLED);
               orders.setCancelReason("订单超时,自动取消");
               orders.setCancelTime(LocalDateTime.now());
               orderMapper.update(orders);

           }
       }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理处于派送中的订单");
        List<Orders> list= orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS,LocalDateTime.now().plusMinutes(-60));
        if(list!=null&&list.size()>0){
            for (Orders orders : list) {
                //将状态改为已取消
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);

            }
        }

    }
}
