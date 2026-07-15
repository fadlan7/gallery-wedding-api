package com.wedding.gallery.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.wedding.gallery..*.*(..))")
    public Object logAndMeasureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] methodArgs = joinPoint.getArgs();

        // 1. Log saat masuk ke method
        log.info("➡️ Entering method: {}", methodName);
        log.info("📥 Arguments: {}", Arrays.toString(methodArgs));

        // 2. Jalankan bisnis logic aslinya
        Object result = joinPoint.proceed();

        // 3. Hitung durasi performa kecepatan method
        long executionTime = System.currentTimeMillis() - startTime;

        // 4. Log saat sukses
        log.info("✅ Method executed successfully: {}", methodName);
        log.info("⏱ Execution time: {} ms", executionTime);

        return result;
    }
}