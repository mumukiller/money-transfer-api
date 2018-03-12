package com.mumukiller.transfer.exception;

public class BusinessException extends ApplicationException {

  public BusinessException(final ErrorCode errorCode) {
    super(errorCode);
  }
}
