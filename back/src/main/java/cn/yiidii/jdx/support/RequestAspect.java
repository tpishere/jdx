package cn.yiidii.jdx.support;

import com.alibaba.fastjson.JSON;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RequestAspect {

    public final HttpServletRequest request;

    @Pointcut("execution(* cn.yiidii.jdx.controller..*.*(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestURI = request.getRequestURI();
        Object[] args = joinPoint.getArgs();
        log.debug("uri: {}, 请求参数为: {}", requestURI, JSON.toJSONString(args));
        Object result = joinPoint.proceed(args);
        log.debug("uri: {}, 响应结果为: {}", requestURI, JSON.toJSONString(result));
        return result;
    }
}
