package com.security_app.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Log request details
        logger.info("Request: [{}] {} {}",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                httpRequest.getProtocol());

        // Log request headers
        if (logger.isDebugEnabled()) {
            httpRequest.getHeaderNames().asIterator().forEachRemaining(headerName -> {
                logger.debug("Request header: {} = {}", headerName, httpRequest.getHeader(headerName));
            });
        }

        // Continue with the filter chain
        long startTime = System.currentTimeMillis();
        chain.doFilter(request, response);
        long duration = System.currentTimeMillis() - startTime;

        // Log response details
        logger.info("Response: {} {} - Completed in {} ms",
                httpResponse.getStatus(),
                httpRequest.getRequestURI(),
                duration);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing RequestResponseLoggingFilter");
    }

    @Override
    public void destroy() {
        logger.info("Destroying RequestResponseLoggingFilter");
    }
}