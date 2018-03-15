package com.mumukiller.transfer.validation;

import com.mumukiller.transfer.dto.AmountDto;
import com.mumukiller.transfer.dto.TransferDto;
import com.mumukiller.transfer.entity.Account;
import com.mumukiller.transfer.exception.BusinessException;
import com.mumukiller.transfer.exception.ErrorCode;
import com.mumukiller.transfer.service.TransferOperationHolder;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import java.math.BigDecimal;

@Resource
@ManagedBean
public class TransferValidatorImpl implements TransferValidator {

  @Override
  public void validate(final TransferOperationHolder holder) {
    final TransferDto transaction = holder.getTransaction();
    final Account source = holder.getSource();
    final Account destination = holder.getDestination();
    final AmountDto transactionAmount = transaction.getAmount();

    if (transactionAmount.getValue().compareTo(BigDecimal.ZERO) <= 0) {
      throw new BusinessException(ErrorCode.TRANSFER_AMOUNT_INVALID);
    }

    if (!source.getCurrency().equals(destination.getCurrency()) ||
            !source.getCurrency().equals(transactionAmount.getCurrency())) {
      throw new BusinessException(ErrorCode.SOURCE_AND_DESTINATION_INCOMPATIBLE_CURRENCY);
    }

    final BigDecimal sourceValue = source.getValue();
    if (sourceValue.compareTo(BigDecimal.ZERO) < 0 ||
            sourceValue.subtract(transactionAmount.getValue()).compareTo(BigDecimal.ZERO) < 0) {
      throw new BusinessException(ErrorCode.SOURCE_INSUFFICIENT_FUNDS);
    }
  }
}
