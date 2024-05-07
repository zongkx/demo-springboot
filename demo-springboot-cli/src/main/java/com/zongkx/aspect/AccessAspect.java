package com.zongkx.aspect;

import com.zongkx.annotation.AccessPermission;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author zongkxc
 * @Description
 * @create 2021/5/31  15:03
 */
@Aspect
@Component
@Slf4j
public class AccessAspect {
    @Pointcut("@annotation(com.zongkx.annotation.AccessPermission)")
    private void cutMethod() {
    }
    @Around("cutMethod()")
    public Object  around(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] params = joinPoint.getArgs();
        AccessPermission declaredAnnotation = getDeclaredAnnotation(joinPoint);
        HttpServletRequest request = null;
        int i = 0;
        for (Object param : params) {
            if(param instanceof HttpServletRequest){
                request = (HttpServletRequest) param;
                break;
            }
            i++;
        }
        assert request != null;
        request.setAttribute("my","my");
        params[i] = request;
//        log.info(" method name " + methodName + " args " + params[0]);
        // 执行源方法
        return joinPoint.proceed(params);
    }
    public AccessPermission getDeclaredAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);
        return objMethod.getDeclaredAnnotation(AccessPermission.class);
    }
}
