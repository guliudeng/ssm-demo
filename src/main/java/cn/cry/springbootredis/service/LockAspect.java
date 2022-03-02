package cn.cry.springbootredis.service;


import cn.cry.springbootredis.utils.LockUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class LockAspect {
    @Autowired
    private LockUtil lockUtil;//注入分布式锁工具类
    //定义业务标识
    private static final String SERVICE_ID = "物流系统->库存管理模块->库存更新服务";
    //定义MyService类minus方法操作的超时时间  10秒
    private static final Long TIMEOUT = 10L;
    //定义MyService类minus方法操作的超时时间单位
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    //环绕通知
    @Around("execution(* cn.cry.springbootredis.service.MyService.minus(..))))")
    public boolean around(ProceedingJoinPoint joinPoint)throws Throwable{
        try{
            boolean getLock = lockUtil.lock(SERVICE_ID,TIMEOUT,TIME_UNIT);//加锁
            if(!getLock){  //没获取到锁
                return false;
            }else {     //获取到锁后执行业务逻辑操作
                if(!(boolean) joinPoint.proceed())
                    throw new SoldOutException();
                return true;
            }
        }catch (Throwable e){
            throw e;
        } finally {
            lockUtil.unlock(SERVICE_ID);//释放锁
        }
    }
    //自定义异常类（卖完了异常）
    class SoldOutException extends RuntimeException {
    }
}