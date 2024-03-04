package org.stepanov.telegram.bot.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Log4j2
public class RequestTelegramOriginatedFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (matches(request, "149.154.160.0/20") ||
            matches(request, "91.108.4.0/22") ||
                matches(request, "127.0.0.1/32")) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("wrong IP address {}", request.getRemoteAddr());
            response.setStatus(404);
        }
    }

    private boolean matches(HttpServletRequest request, String subnet) {
        IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(subnet);
        return ipAddressMatcher.matches(request);
    }
}
