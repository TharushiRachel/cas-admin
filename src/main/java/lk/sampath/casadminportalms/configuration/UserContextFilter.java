package lk.sampath.casadminportalms.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lk.sampath.casadminportalms.dto.usersession.UserContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class UserContextFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      UserContext.setUserId(request.getHeader("X-USER-ID"));
      UserContext.setSolId(request.getHeader("X-SOL-ID"));
      UserContext.setUsername(request.getHeader("X-USERNAME"));
      UserContext.setSecurityClass(request.getHeader("X-SC"));
      UserContext.setDisplayName(request.getHeader("X-DISPLAY-NAME"));
      filterChain.doFilter(request, response);
    } finally {
      UserContext.clear();
    }
  }
}
