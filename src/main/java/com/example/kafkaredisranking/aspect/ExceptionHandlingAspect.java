package com.example.kafkaredisranking.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingAspect.class);

    @Around("execution(* com.example.kafkaredisranking.service..*(..))")
    public Object handleServiceException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            logger.error("Exception occurred in service method: {}, with message: {}", joinPoint.getSignature(), e.getMessage());
            throw e;
        }
    }

    @Around("execution(* com.example.kafkaredisranking.repository..*(..))")
    public Object handleRepositoryException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            logger.error("Exception occurred in repository method: {}, with message: {}", joinPoint.getSignature(), e.getMessage());
            throw e;
        }
    }

    @Around("execution(* org.springframework.boot.CommandLineRunner.run(..))")
    public Object handleCommandLineRunnerException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            logger.error("Exception occurred in CommandLineRunner method: {}, with message: {}", joinPoint.getSignature(), e.getMessage());
            throw e;
        }
    }
}
