package cn.cry.springbootredis.service;


import cn.cry.springbootredis.bo.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 业务操作类
 */
@Component
public class MyService {
    @Autowired
    private ApplicationContext applicationContext;
    /**
     * 高并发修改资源的方法
     * 该资源例如：一条库存记录，该数据存储在本机内存中
     * 库存减1成功  返回true  其他情况返回false
     */
    public boolean minus(){
        Inventory inventory = (Inventory) applicationContext.getBean("inventory");
        Long surplus = inventory.getSurplus();
        Long lucyNumber = inventory.getLucyNumber();
        if(surplus > 0){
            inventory.setSurplus(--surplus);
            inventory.setLucyNumber(++lucyNumber);
            return true;
        }
        return false;
    }
}

