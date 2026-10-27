package com.thermcampos.web;

import io.javalin.config.JavalinConfig;
import java.util.UUID;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class RequestLogging {

  public static final String REQUEST_ID = "requestId";
  public static final String REQUEST_ID_HEADER = "X-Request-Id";

  private static final Logger log = LoggerFactory.getLogger(RequestLogging.class);

  // Only trust caller-provided ids that are short and safe to put in logs
  private static final Pattern SAFE_ID = Pattern.compile("[A-Za-z0-9-]{1,64}");

  public static void register(JavalinConfig c) {
    // Runs first: every log line of this request carries the id via MDC
    c.routes.before(ctx -> {
      String incoming = ctx.header(REQUEST_ID_HEADER);
      String requestId = incoming != null && SAFE_ID.matcher(incoming).matches()
          ? incoming
          : UUID.randomUUID().toString();

      MDC.put(REQUEST_ID, requestId);
      ctx.header(REQUEST_ID_HEADER, requestId);
    });

    // Runs last (after handlers, exception handlers and after-filters)
    c.requestLogger.http((ctx, ms) -> {
      try {
        log.atInfo()
            .addKeyValue("method", ctx.method().name())
            .addKeyValue("path", ctx.path())
            .addKeyValue("status", ctx.statusCode())
            .addKeyValue("durationMs", ms)
            .log("Request completed");
      } finally {
        MDC.remove(REQUEST_ID);
      }
    });
  }
}
