package com.thermcampos.health;

import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class HealthCheckHandler {
    
    public void getHealth(Context ctx) {
        ctx.addHeader(ContentType.APPLICATION_JSON.name(), ContentType.APPLICATION_JSON.getMimeType());
        ctx.status(HttpStatus.OK);
        ctx.result("{\"status\":\"OK\"}");
    }
}
