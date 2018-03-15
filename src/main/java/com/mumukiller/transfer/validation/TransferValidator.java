package com.mumukiller.transfer.validation;

import com.mumukiller.transfer.service.TransferOperationHolder;

public interface TransferValidator {

  void validate(final TransferOperationHolder holder);
}
