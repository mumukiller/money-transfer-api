package com.mumukiller.transfer.exception.handler;

import com.mumukiller.transfer.exception.dto.ErrorResponseDto;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public abstract class BaseExceptionHandler<T extends Exception> implements ExceptionMapper<T> {

  public final Response build(final Response.Status status, final String message) {
    return Response.status(status)
            .entity(ErrorResponseDto.builder()
                    .message(message)
                    .build())
            .type(MediaType.APPLICATION_JSON)
            .build();
  }
}
