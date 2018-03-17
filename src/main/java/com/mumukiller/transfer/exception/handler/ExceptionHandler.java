package com.mumukiller.transfer.exception.handler;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class ExceptionHandler extends BaseExceptionHandler<Exception> {
  public Response toResponse(final Exception e) {
    log.error(e.getMessage());
    return build(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
  }
}
