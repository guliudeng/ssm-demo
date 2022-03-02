package cn.cry.springbootredis.bo;

import cn.cry.springbootredis.utils.LockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

public class UserThread implements Runnable{
    @Autowired
    LockUtil lockUtil;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public void run() {
        String key = "REDIS_LOCK";
        //获取锁
        boolean lock = lockUtil.lock(key, 10L, TimeUnit.SECONDS);
        if (! lock) {
            System.out.println("获取锁失败"+Thread.currentThread().getName());
            return;
        }
        try {
            System.out.println("获取锁成功"+Thread.currentThread().getName());
            Thread.sleep(2000);
            ValueOperations operations = redisTemplate.opsForValue();
            operations.set("gld","谷刘邓");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println("开始释放锁");
            boolean unlock = lockUtil.unlock(key);
            if (unlock) {
                System.out.println("释放锁成功"+Thread.currentThread().getName());
            }
        }
    }
}
