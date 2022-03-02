package cn.cry.springbootredis.service;


import cn.cry.springbootredis.bo.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 模拟抢购线程
 */
@Component
@Scope("prototype")
public class MyTask implements Runnable{
    @Autowired
    private MyService myService;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run() {
        Inventory inventory = (Inventory) applicationContext.getBean("inventory");
        try{
            //抢购失败，歇半秒后再抢
            while(!myService.minus()){
                String s = String.format("【%s】抱歉，当前拥堵，稍后再试!!",Thread.currentThread().getName());
                System.out.println(s);
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                }
            }
            //抢到之后自动停止
            String s = String.format("恭喜【%s】抢到了一辆【%s】，库存余量为：【%s】，当前幸运用户数为：【%s】",
                    Thread.currentThread().getName(),inventory.getCommodityName(),
                    inventory.getSurplus(),inventory.getLucyNumber());
            System.out.println(s);
        }catch (LockAspect.SoldOutException e){
            //没抢到。。。
            String s = String.format("【%s】，很遗憾，你没抢到，活动已结束...",Thread.currentThread().getName());
            System.out.println(s);
        }
    }
}