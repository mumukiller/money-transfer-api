package com.mumukiller.transfer.service;

import com.mumukiller.transfer.dto.TransferDto;
import com.mumukiller.transfer.entity.Account;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransferOperationHolder {
  private final TransferDto transaction;
  private final Account source;
  private final Account destination;
}
