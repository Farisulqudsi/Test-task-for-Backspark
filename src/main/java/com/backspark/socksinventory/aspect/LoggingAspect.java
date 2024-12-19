package com.backspark.socksinventory.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(public * com.backspark.socksinventory.service.*.*(..))")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("Calling method: {} with args: {}", methodName, args);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            log.error("Exception in method: {} with message: {}", methodName, t.getMessage());
            throw t;
        }

        long elapsedTime = System.currentTimeMillis() - start;
        log.info("Method {} returned: {} in {} ms", methodName, result, elapsedTime);

        return result;
    }
}
