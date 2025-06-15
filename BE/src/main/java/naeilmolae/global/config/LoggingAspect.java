package naeilmolae.global.config;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.UUID;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object aroundController(ProceedingJoinPoint pjp) throws Throwable {
        // 1) 현재 HTTP 요청 가져오기
        RequestAttributes reqAttr = RequestContextHolder.getRequestAttributes();
        String logId = null;

        if (reqAttr instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) reqAttr).getRequest();

            // 2) X-Request-ID 헤더 조회 (대소문자 무관)
            logId = Optional.ofNullable(request.getHeader("X-Request-ID"))
                    .filter(id -> !id.isBlank())
                    .orElse(null);
        }

        // 3) 헤더 없으면 UUID 생성
        if (logId == null) {
            logId = UUID.randomUUID().toString();
        }

        // 4) MDC에 삽입
        MDC.put("logId", logId);

        try {
            return pjp.proceed();
        } finally {
            // 5) 반드시 제거
            MDC.remove("logId");
        }
    }
}