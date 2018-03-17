package com.mumukiller.transfer.exception.handler;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionHandler extends BaseExceptionHandler<NotFoundException> {
  public Response toResponse(final NotFoundException e) {
    return build(Response.Status.NOT_FOUND, e.getMessage());
  }
}
