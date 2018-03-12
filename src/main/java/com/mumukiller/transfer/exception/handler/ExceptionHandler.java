package com.mumukiller.transfer.exception.handler;

import com.mumukiller.transfer.exception.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class ExceptionHandler implements ExceptionMapper<Exception> {
  public Response toResponse(final Exception e) {
    log.error(e.getMessage());
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(ErrorResponseDto.builder()
                    .message(e.getMessage())
                    .build())
            .type(MediaType.APPLICATION_JSON)
            .build();
  }
}
