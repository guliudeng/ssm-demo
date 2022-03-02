package cn.cry.springbootredis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.DefaultScriptExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Transactional
public class LockUtil {
    @Autowired
    private RedisTemplate redisTemplate;
    //线程id
    private ThreadLocal<String> threadIdBox = new ThreadLocal<>();

    /**
     * 获取分布式锁
     * @param key 锁名
     * @param timeout 锁超时时间
     * @param timeUnit 时间单位
     * @return 创建锁成功返回true，失败返回false
     */
    public boolean lock(String key, Long timeout, TimeUnit timeUnit) {
        try {
            threadIdBox.set(UUID.randomUUID().toString());
            //如果KEY不存在，则创建KEY
            //KEY不存在，返回true  存在返回false
            return redisTemplate.opsForValue().setIfAbsent(key, threadIdBox.get(), timeout, timeUnit);
        } catch (Exception e) {
            //引发任何异常，spring将不会创建KEY，直接返回false
            threadIdBox.remove();
            return false;
        }
    }
    /**
     * 释放锁
     * @param serviceId  KEY  用于标识高并发修改资源的业务
     * @return
     */
    public boolean unlock(String serviceId) {
        //当前线程没有生成线程标识，说明压根没有创建过KEY
        if (threadIdBox.get() == null) return true;
        try {
            //lua脚本(保证查询和删除的原子性)
            //如果根据serviceId查找出的值与当前线程的标识码一致，则删除该KEY并返回删除结果，没找到KEY直接返回false
            String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
            DefaultRedisScript redisScript = new DefaultRedisScript(luaScript);
            redisScript.setResultType(Boolean.class);
            DefaultScriptExecutor defaultScriptExecutor = new DefaultScriptExecutor(redisTemplate);
            return (boolean) defaultScriptExecutor.execute(redisScript, Arrays.asList(serviceId), threadIdBox.get());
        } catch (Exception e) {
            //引发任何异常，spring将不会执行删除操作，直接返回false
            return false;
        } finally {
            threadIdBox.remove();
        }
    }

}
