package com.bol.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;


/**
 * A <i>LoggingConfig</i>. This class has responsibility to log every request come and go to the controller layer
 * and also every exception log for further inspection<p>
 *
 * @author Omid Rostami
 */


@Aspect
@Component
@Slf4j
public class LoggingConfig {

    @AfterThrowing(value = "execution(* com.bol..*.*(..))", throwing = "ex")
    public void logError(Exception ex) {
        log.error("log Error {} " + ex);
    }

    @After(value = "@within(org.springframework.web.bind.annotation.RestController) ")
    public void logAfter(JoinPoint joinPoint) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();

            log.info("Entering in Method: {}", joinPoint.getSignature().getName());
            log.info("Class Name: {}", joinPoint.getSignature().getDeclaringTypeName());
            log.info("Arguments:  {}", Arrays.toString(joinPoint.getArgs()));
            log.info("Target class: {}", joinPoint.getTarget().getClass().getName());

            if (Objects.nonNull(request.getMethod()) && Objects.nonNull(request.getHeaderNames())) {
                log.info("Start Header Section of request ");
                log.info("Method Type: {}", request.getMethod());
                Enumeration headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String headerName = (String) headerNames.nextElement();
                    String headerValue = request.getHeader(headerName);
                    log.info("Header Name: " + headerName + " Header Value : " + headerValue);
                }
                log.info("Request Path info: {}", request.getServletPath());
                log.info("End Header Section of request ");
            }
        } catch (Exception e) {
            log.info("Error during reading body and header ");
        }
    }
}
