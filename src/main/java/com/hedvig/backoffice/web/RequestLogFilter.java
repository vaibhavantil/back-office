package com.hedvig.backoffice.web;

import io.sentry.Sentry;
import io.sentry.event.UserBuilder;
import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import lombok.val;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
public class RequestLogFilter implements Filter {

  private static final String USER_NAME_KEY = "userName";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    try {
      if (OAuth2Authentication.class.isInstance(authentication)) {
        OAuth2Authentication oauth = (OAuth2Authentication) authentication;
        val name = (Map) oauth.getUserAuthentication().getDetails();
        MDC.put(USER_NAME_KEY, (String) name.get("email"));
        Sentry.getContext().setUser(new UserBuilder().setId((String)name.get("email")).build());
      }

      chain.doFilter(request, response);
    } finally {
      if (authentication != null) {
        MDC.remove(USER_NAME_KEY);
      }
    }
  }

  @Override
  public void destroy() {}
}
