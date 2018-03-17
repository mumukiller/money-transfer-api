package com.mumukiller.transfer.exception.handler;

import com.mumukiller.transfer.exception.BusinessException;
import com.mumukiller.transfer.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class BusinessExceptionHandler extends BaseExceptionHandler<BusinessException> {
  public Response toResponse(final BusinessException e) {
    log.error(e.getMessage());
    if (ErrorCode.getValidationErrorCodes().contains(e.getErrorCode())) {
      return build(Response.Status.PRECONDITION_FAILED, e.getErrorCode().name());
    }

    return build(Response.Status.INTERNAL_SERVER_ERROR, e.getErrorCode().name());

  }
}
