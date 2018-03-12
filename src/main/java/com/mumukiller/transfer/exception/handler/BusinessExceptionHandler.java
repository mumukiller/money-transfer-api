package com.mumukiller.transfer.exception.handler;

import com.mumukiller.transfer.exception.BusinessException;
import com.mumukiller.transfer.exception.ErrorCode;
import com.mumukiller.transfer.exception.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class BusinessExceptionHandler implements ExceptionMapper<BusinessException> {
  public Response toResponse(final BusinessException e) {
    log.error(e.getMessage());

    final ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
            .message(e.getErrorCode().name())
            .build();

    if (ErrorCode.getValidationErrorCodes().contains(e.getErrorCode())){
      return Response.status(Response.Status.PRECONDITION_FAILED)
              .entity(errorResponseDto)
              .type(MediaType.APPLICATION_JSON)
              .build();
    }

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(errorResponseDto)
            .type(MediaType.APPLICATION_JSON)
            .build();
  }
}
