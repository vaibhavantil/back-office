package com.hedvig.backoffice.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public class SecureFilter implements Filter {

  private int defaultPort;
  private int httpPort;
  private String defaultPortStr;
  private String httpPortStr;
  private boolean httpsRedirect;

  public SecureFilter(int defaultPort, int httpPort, boolean httpsRedirect) {
    this.defaultPort = defaultPort;
    this.httpPort = httpPort;

    this.defaultPortStr = Integer.toString(defaultPort);
    this.httpPortStr = Integer.toString(httpPort);

    this.httpsRedirect = httpsRedirect;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) resp;
    String fullUrl = getFullURL(request);

    if (request.getLocalPort() == httpPort && httpsRedirect) {
      String locationHeader = null;

      if (fullUrl.contains(httpPortStr)) {
        locationHeader = fullUrl.replace(httpPortStr, defaultPortStr);
      } else if (!fullUrl.contains("https")) {
        locationHeader = fullUrl.replace("http", "https");
      }

      if (locationHeader != null) {
        response.setStatus(HttpStatus.FOUND.value());
        response.setHeader("Location", locationHeader);
        return;
      }
    }

    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void destroy() {}

  public static String getFullURL(HttpServletRequest request) {
    StringBuffer requestURL = request.getRequestURL();
    String queryString = request.getQueryString();

    if (queryString == null) {
      return requestURL.toString();
    } else {
      return requestURL.append('?').append(queryString).toString();
    }
  }
}
