package com.mumukiller.transfer.exception.handler;

import com.mumukiller.transfer.exception.dto.ErrorResponseDto;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {
  public Response toResponse(final NotFoundException e) {
    return Response.status(Response.Status.NOT_FOUND)
            .entity(ErrorResponseDto.builder()
                    .message(e.getMessage())
                    .build())
            .type(MediaType.APPLICATION_JSON)
            .build();
  }
}
