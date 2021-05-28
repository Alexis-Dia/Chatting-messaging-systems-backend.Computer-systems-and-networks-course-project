package alexeyd.com.configurations;

import alexeyd.com.model.Report;
import alexeyd.com.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;

@Component
class MyPayloadCapturingFilter {

    @Autowired
    private ReportRepository reportRepository;

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> saveLoginOriginFilter() {
        OncePerRequestFilter filter = new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain)
                    throws ServletException, IOException {

                String name = "anonymousUser";
                SecurityContext context = SecurityContextHolder.getContext();
                if (context != null) {
                    name = context.getAuthentication().getName();
                }
                int status = response.getStatus();
                String requestURI = request.getRequestURI();
                final String method = request.getMethod();

                Report report = new Report();
                report.setId(System.currentTimeMillis());
                report.setMethod(method);
                report.setUrl(requestURI);
                report.setLocalDateTime(LocalDateTime.now());
                report.setUserName(name);
                report.setCode(String.valueOf(status));
                reportRepository.save(report).block();


                filterChain.doFilter(request, response);
            }
        };
        FilterRegistrationBean<OncePerRequestFilter> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return bean;
    }

}
