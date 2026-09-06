package com.thermcampos.health;

import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class HealthCheckHandler {
    
    public static void getHealthHandler(Context ctx) {
        ctx.status(HttpStatus.OK);
        ctx.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        ctx.result("{\"status\":\"OK\"}");
    }
}
