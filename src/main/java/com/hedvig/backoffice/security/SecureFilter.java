package com.hedvig.backoffice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SecureFilter implements Filter {

    private int defaultPort;
    private int httpPort;
    private String defaultPortStr;
    private String httpPortStr;

    public SecureFilter(int defaultPort, int httpPort) {
        this.defaultPort = defaultPort;
        this.httpPort = httpPort;

        this.defaultPortStr = Integer.toString(defaultPort);
        this.httpPortStr = Integer.toString(httpPort);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String fullUrl = getFullURL(request);
        log.info("receive request host: " + fullUrl
                + " port: " + request.getServerPort()
                + " local:" + request.getLocalPort()
                + " remote: " + request.getRemotePort());

        if (request.getLocalPort() == httpPort) {
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
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

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
