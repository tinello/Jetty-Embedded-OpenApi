package ar.com.tinello.api.web;

import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.server.CookieCache;
import org.openapi4j.operation.validator.model.Request;
import org.openapi4j.operation.validator.model.impl.Body;
import org.openapi4j.operation.validator.model.impl.DefaultRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Enumeration;


import static java.util.Objects.requireNonNull;

public abstract class ServletRequest implements Request {
  private static final String HTTP_GET = "GET";
  private static final String ERR_MSG = "A HttpServletRequest is required";

  private ServletRequest() {
  }

  /**
   * Creates a wrapped request from a servlet request.
   *
   * @param hsr The given server request.
   * @return The wrapped request to work this.
   */
  public static Request of(final org.eclipse.jetty.server.Request hsr) {
    final var a = hsr.read();
    final var b = a.getByteBuffer();
    final var c = b.array();
    return of(hsr, new ByteArrayInputStream(c));
  }

  /**
   * Creates a wrapped request from a servlet request.
   *
   * @param hsr  The given server request.
   * @param body The body to consume.
   * @return The wrapped request to work this.
   */
  public static Request of(final org.eclipse.jetty.server.Request hsr, InputStream body) {
    requireNonNull(hsr, ERR_MSG);

    // Method & path
    final DefaultRequest.Builder builder = new DefaultRequest.Builder(
      hsr.getHttpURI().toString(),
      Request.Method.getMethod(hsr.getMethod()));

    // Query string or body
    if (HTTP_GET.equalsIgnoreCase(hsr.getMethod())) {
      builder.query(hsr.getHttpURI().getQuery());
    } else {
      builder.body(Body.from(body));
    }

    // Cookies
    if (CookieCache.getCookies(hsr) != null) {
      for (HttpCookie cookie : CookieCache.getCookies(hsr)) {
        builder.cookie(cookie.getName(), cookie.getValue());
      }
    }

    // Headers
    Enumeration<String> headerNames = hsr.getHeaders().getFieldNames();
    if (headerNames != null) {
      while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        Enumeration<String> headerValues = hsr.getHeaders().getValues(headerName);

        while (headerValues.hasMoreElements()) {
          builder.header(headerName, headerValues.nextElement());
        }
      }
    }

    return builder.build();
  }
}