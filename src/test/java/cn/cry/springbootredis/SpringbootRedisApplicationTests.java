package cn.cry.springbootredis;

import cn.cry.springbootredis.bo.UserThread;
import cn.cry.springbootredis.utils.LockUtil;
import jdk.nashorn.internal.ir.CallNode;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * 多个线程同时执行 一个操作
 */

@SpringBootTest
class SpringbootRedisApplicationTests {
    private static SimpleDateFormat stf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    LockUtil lockUtil;
    @Test
    void contextLoads() {
    }
    @Test
    public void redisDemo(){
      UserThread userThread = new UserThread();
        Thread thread1 = new Thread(userThread, "a");

    }
    public void  op(int i){
    String key = "REDIS_LOCK";
    //获取锁
    boolean lock = lockUtil.lock(key, 30L, TimeUnit.SECONDS);
        System.out.println(lock);
    //判断是否获取锁
    if (!lock) {
        System.out.println("获取锁失败");
        return;
    }
    try {
        System.out.println("获取锁成功，执行业务");
        Thread.sleep(1000);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("gu"+i,"故六点",300,TimeUnit.SECONDS);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }finally {
        //释放锁
        System.out.println("释放锁");
        lockUtil.unlock(key);
    }
}
@Test
public void test001(){
    String firstDay = getBeforeMonthFirstDay(-2);
    String lastDay = getBeforeMonthLastDay(0);
    String yearStart = getYearStart();
    String yearEnd = getYearEnd();

    System.out.println(firstDay);
    System.out.println(lastDay);
    System.out.println(yearStart);
    System.out.println(yearEnd);
}

    public static String getBeforeMonthFirstDay(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,month);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        int minimum = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH,minimum);
        Date time = calendar.getTime();
        return stf.format(time);
    }
    public static String getBeforeMonthLastDay(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,month);
        int maximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH,maximum);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        return stf.format(calendar.getTime());
    }
public static String getYearStart(){
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR,getNowYear());
    calendar.set(Calendar.MONTH,Calendar.JANUARY);
    calendar.set(Calendar.DATE,1);
    calendar.set(Calendar.HOUR_OF_DAY,0);
    calendar.set(Calendar.MINUTE,0);
    calendar.set(Calendar.SECOND,0);
    return stf.format(calendar.getTime());
}
public static String getYearEnd(){
        Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR,getNowYear());
    calendar.set(Calendar.MONTH,Calendar.DECEMBER);
    calendar.set(Calendar.DATE,31);
    calendar.set(Calendar.HOUR_OF_DAY,23);
    calendar.set(Calendar.MINUTE,59);
    calendar.set(Calendar.SECOND,59);
    return stf.format(calendar.getTime());
}
//获取今年是哪一年
      public static Integer getNowYear() {
                  Date date = new Date();
                   GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
                   gc.setTime(date);
                  return Integer.valueOf(gc.get(1));
                }
      @Test
      public void test002(){
        String str = "";
        int a = 11;
          int i = a / 10;
          if (i == 0) {
              str = "0"+a;
          }else {
              str = String.valueOf(a);
          }
          System.out.println(str);
      }

      @Test
      public void test003(){
        String str = "2022-02-20 20:00:32";
          String month = str.substring(0, 7);
          String day = str.substring(0, 10);
          System.out.println(month);
          System.out.println(day);
      }
    @Test
    public void test004(){
        int round = Math.round(32432 / 64432);
        System.out.println(round);
        long round1 = Math.round(32432 / (64432 + 0.0) * 100);
        System.out.println(round1);

    }
}
