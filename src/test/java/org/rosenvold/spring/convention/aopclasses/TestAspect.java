package org.rosenvold.spring.convention.aopclasses;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TestAspect
{
    

    @Around(value="execution(* testMethod(..))", argNames = "pjp")
    @Order(1)
    public Object clearCache( ProceedingJoinPoint pjp ) throws Throwable {
        Object target = pjp.getTarget();
        if (target instanceof DefaultTestInterface){
              ((DefaultTestInterface)target).setAopCalled();
        }
        return pjp.proceed();
    }

}
