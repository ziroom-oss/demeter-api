package com.ziroom.tech.demeterapi.config;

import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lipp3
 * @date 2021/6/25 17:16
 * @Description
 */
@Slf4j
@Aspect
@Component
public class LoggerInterceptor {

    @Pointcut("@annotation(com.ziroom.tech.demeterapi.config.RecordLogger)")
    public void loggerCut() { }

    private Map<String, Long> timeMap = new HashMap<>();

    /**
     *
     *@param joinPoint
     *@return
     *@throws
     *
     *@author
     *@date 2020/12/25 20:31
     */
    @Before("loggerCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        //类名
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        //方法名称
        String methodName = joinPoint.getSignature().getName();
        //方法开始时间
        timeMap.put(Thread.currentThread().getId() + "" + methodName + "start", System.currentTimeMillis());
        //防止参数转换报错
        logBefore(joinPoint, className, methodName);
    }

    static void logBefore(JoinPoint joinPoint, String className, String methodName) {
        try {
            Object[] param = new Object[joinPoint.getArgs().length];
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                if (joinPoint.getArgs()[i] != null && !(joinPoint.getArgs()[i] instanceof Proxy)) {
                    param[i] = joinPoint.getArgs()[i];
                } else {
                    param[i] = null;
                }
            }
            log.info("【" + className + ":" + methodName + "】开始执行");
        } catch (Exception e) {
            log.warn("日志参数转换异常，不影响业务逻辑！信息：" + e.getMessage());
        }
    }

    /**
     *
     *@param joinPoint
     *@param ret
     *@return
     *@throws
     *
     *@author
     *@date 2020/12/25 20:32
     */
    @AfterReturning(returning = "ret", pointcut = "loggerCut()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        //类名
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        //方法名称
        String methodName = joinPoint.getSignature().getName();
        //防止参数转换报错
        try {
            //方法开始时间
            timeMap.put(Thread.currentThread().getId() + "" + methodName + "end", System.currentTimeMillis());
            Long cost = timeMap.get(Thread.currentThread().getId() + "" + methodName + "end") - timeMap.get(Thread.currentThread().getId() + "" + methodName + "start");
            log.info("【" + className + ":" + methodName + "】执行完毕,"+ ";花费时间" + cost + "ms");
        } catch (Exception e) {
            log.warn("日志参数转换异常，不影响业务逻辑！信息：" + e.getMessage());
        } finally {
            timeMap.remove(Thread.currentThread().getId() + "" + methodName + "start");
            timeMap.remove(Thread.currentThread().getId() + "" + methodName + "end");
        }

    }

    /**
     *
     *@param joinPoint
     *@param e
     *@return
     *@throws
     *
     *@author
     *@date 2020/12/25 20:32
     */
    @AfterThrowing(pointcut = "loggerCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        //类名
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        //方法名称
        String methodName = joinPoint.getSignature().getName();
        try {
            //方法开始时间
            timeMap.put(Thread.currentThread().getId() + "" + methodName + "end", System.currentTimeMillis());
            Long cost = timeMap.get(Thread.currentThread().getId() + "" + methodName + "end") - timeMap.get(Thread.currentThread().getId() + "" + methodName + "start");
            log.info("【ServiceInterceptor】doAfterThrowing:" + e.getClass().getName());
            if(!(e instanceof BusinessException)){
                log.error("【ServiceInterceptor】异常:", e);
            }
            log.info("【" + className + ":" + methodName + "】执行完毕" + ";花费时间" + cost + "ms;" +  "异常信息:" + e.getMessage());
        } catch (Exception e1) {
            log.warn("日志参数转换异常，不影响业务逻辑！信息：" + e.getMessage());
        } finally {
            timeMap.remove(Thread.currentThread().getId() + "" + methodName + "start");
            timeMap.remove(Thread.currentThread().getId() + "" + methodName + "end");
        }
    }
}

