package com.mumukiller.transfer.exception;

import java.util.EnumSet;
import java.util.Set;

public enum ErrorCode {
  NOT_FOUND,

  TRANSFER_FAILED,
  TRANSFER_AMOUNT_INVALID,
  SOURCE_AND_DESTINATION_INCOMPATIBLE_CURRENCY,
  SOURCE_INSUFFICIENT_FUNDS;

  public static Set<ErrorCode> getValidationErrorCodes() {
    return EnumSet.of(TRANSFER_AMOUNT_INVALID,
            SOURCE_AND_DESTINATION_INCOMPATIBLE_CURRENCY,
            SOURCE_INSUFFICIENT_FUNDS);
  }
}
