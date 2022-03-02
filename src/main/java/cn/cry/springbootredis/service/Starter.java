package cn.cry.springbootredis.service;

import cn.cry.springbootredis.bo.Inventory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Starter {
    public static void main(String[] args)throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
       context.setConfigLocation("classpath:spring/spring-*.xml");
        context.refresh();
        //定义一个库存实体
        Inventory inventory = (Inventory) context.getBean("inventory");
        inventory.setCommodityId(656232323265L);
        inventory.setCommodityName("兰博基尼（豪华镶钻版）1.0元抢购版");
        inventory.setSurplus(800L);       //库存800
        inventory.setLucyNumber(0L);      //幸运用户数
        //定义1000个人来抢购商品，一个人限购一件，注定有200人抢不到
        for(int  i = 0 ; i < 1000 ; i ++){
            MyTask person = context.getBean("myTask", MyTask.class);
            new Thread(person,"用户"+i).start();
        }
    }
}
