package com.itheima.controller.advice;

import com.itheima.domain.SysLog;
import com.itheima.service.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@Aspect
public class LogAdvice {

    @Autowired
    private LogService logService;

    @Autowired
    private HttpServletRequest request;

    @Around("execution(* com.itheima.controller.*.*(..))")
    public Object around(ProceedingJoinPoint pjp){
        Object obj = null;
        try {
            //得到当前目标对象的类型
            Class clazz = pjp.getTarget().getClass();
            //得到访问时间
            Date visitTime = new Date();
            //得到参数
            Object[] args = pjp.getArgs();
            //放行方法
            obj = pjp.proceed(args);
            if(!pjp.getSignature().getName().equals("initBinder")){
                //提供日志对象
                SysLog log = new SysLog();
                log.setVisitTime(visitTime);
                log.setUsername(request.getRemoteUser());
                log.setUrl(request.getRequestURI());
                log.setIp(request.getRemoteAddr());
                log.setExecutionTime(new Date().getTime()-visitTime.getTime());
                log.setMethod("类名为："+clazz.getName()+"，方法名为"+pjp.getSignature().getName());//类名为：XXX，方法名为：XXX
                //把日志对象入库
                logService.save(log);
            }
        }catch (Throwable t){
            //把403异常恢复到原来的异常去
            if(t.getMessage().equals("Access is denied")){
                throw new AccessDeniedException("权限不足");
            }
            t.printStackTrace();
        }
        return obj;
    }


}
