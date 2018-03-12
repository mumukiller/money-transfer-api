package com.mumukiller.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {
  private String accountSourceId;
  private String accountDestinationId;
  private AmountDto amount;
}
