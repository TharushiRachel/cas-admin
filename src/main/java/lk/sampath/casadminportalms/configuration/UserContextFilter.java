package lk.sampath.casadminportalms.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.sampath.casadminportalms.dto.userSession.UserContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            UserContext.setUserId(
                    request.getHeader("X-USER-ID"));
            UserContext.setSolId(
                    request.getHeader("X-SOL-ID"));
            UserContext.setUsername(
                    request.getHeader("X-USERNAME"));
            UserContext.setSecurityClass(
                    request.getHeader("X-SC"));
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
