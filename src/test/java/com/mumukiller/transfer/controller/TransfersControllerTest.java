package com.mumukiller.transfer.controller;

import com.mumukiller.transfer.MoneyTransferApplication;
import com.mumukiller.transfer.configuration.TestApplicationBinder;
import com.mumukiller.transfer.dto.AccountDto;
import com.mumukiller.transfer.dto.AmountDto;
import com.mumukiller.transfer.dto.TransferDto;
import com.mumukiller.transfer.exception.ErrorCode;
import com.mumukiller.transfer.exception.dto.ErrorResponseDto;
import com.mumukiller.transfer.service.TransferOperationHolder;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

public class TransfersControllerTest extends BaseControllerTest {

  private final static TestApplicationBinder APPLICATION_BINDER = new TestApplicationBinder();

  @Override
  public Application buildApp() {
    return new MoneyTransferApplication(APPLICATION_BINDER);
  }

  @Test
  public void updateDuringTransfer() {
    final BigDecimal value = new BigDecimal(100);

    final AccountDto accountSource = save(AccountDto.builder()
            .username("Petr")
            .currency("RUR")
            .value(value)
            .build());

    final AccountDto accountDestination = save(AccountDto.builder()
            .username("Ivan")
            .currency("RUR")
            .value(value)
            .build());

    //update source during transaction using validation call
    doAnswer(invocation -> {
      validateTableLock(accountSource);
      return null;
    }).when(APPLICATION_BINDER.getMockTransferValidator()).validate(any(TransferOperationHolder.class));


    final Response response = target(UriVariable.TRANSFER_PATH)
            .request()
            .post(Entity.entity(TransferDto.builder()
                    .accountSourceId(accountSource.getId())
                    .accountDestinationId(accountDestination.getId())
                    .amount(AmountDto.builder()
                            .currency("RUR")
                            .value(value)
                            .build())
                    .build(), MediaType.APPLICATION_JSON));

    assertEquals(HttpStatus.OK_200, response.getStatus());
  }

  private void validateTableLock(final AccountDto accountSource){
    final Response response = target(UriVariable.ACCOUNTS_PATH + "/" + accountSource.getId())
            .request()
            .post(Entity.entity(AccountDto.builder()
                    .id(accountSource.getId())
                    .username("Ivan II")
                    .currency("RUR")
                    .value(BigDecimal.ZERO)
                    .build(), MediaType.APPLICATION_JSON));

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatus());

    final ErrorResponseDto error = response
            .readEntity(ErrorResponseDto.class);

    assertNotNull(error);
    assertEquals(error.getMessage(), ErrorCode.TRANSFER_ALREADY_IN_PROGRESS.name());
  }
}
