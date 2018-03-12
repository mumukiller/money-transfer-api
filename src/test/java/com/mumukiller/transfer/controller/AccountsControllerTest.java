package com.mumukiller.transfer.controller;

import com.mumukiller.transfer.MoneyTransferApplication;
import com.mumukiller.transfer.dto.AccountDto;
import com.mumukiller.transfer.dto.AmountDto;
import com.mumukiller.transfer.dto.TransferDto;
import com.mumukiller.transfer.exception.ErrorCode;
import com.mumukiller.transfer.exception.dto.ErrorResponseDto;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class AccountsControllerTest extends JerseyTest {

  public final static GenericType<List<AccountDto>> ACCOUNTS_LIST_GENERIC_TYPE = new GenericType<List<AccountDto>>() {
  };

  @Override
  protected Application configure() {
    set(TestProperties.LOG_TRAFFIC, true);
    set(TestProperties.DUMP_ENTITY, true);
    return new MoneyTransferApplication();
  }

  @Test
  public void notFound() {
    final Response response = target(UriVariable.ACCOUNTS_PATH + "/1")
            .request()
            .get();

    assertEquals(response.getStatus(), HttpStatus.NOT_FOUND_404);
  }

  @Test
  public void okFetch() {
    final BigDecimal expectedValue = new BigDecimal(100);
    final String expectedUsername = "Petr";
    final String expectedCurrency = "USD";

    final AccountDto account = save(AccountDto.builder()
            .username(expectedUsername)
            .currency(expectedCurrency)
            .value(expectedValue)
            .build());

    assertNotNull(account);
    assertNotNull(account.getId());
    assertEquals(account.getUsername(), expectedUsername);
    assertEquals(account.getCurrency(), expectedCurrency);
    assertEquals(account.getValue(), expectedValue);

    final Response searchResponse = target(UriVariable.ACCOUNTS_PATH + "/" + account.getId())
            .request()
            .get();

    assertEquals(searchResponse.getStatus(), HttpStatus.OK_200);

    final AccountDto accountCreated = searchResponse
            .readEntity(AccountDto.class);

    assertNotNull(accountCreated.getId());
    assertEquals(account.getId(), accountCreated.getId());
    assertEquals(account.getUsername(), accountCreated.getUsername());
    assertEquals(account.getCurrency(), accountCreated.getCurrency());
    assertEquals(account.getValue(), accountCreated.getValue());
  }

  @Test
  public void okFetchAll() {
    IntStream.range(0, 12)
            .forEach(i -> save(AccountDto.builder()
                    .username("User " + i)
                    .currency("RUR")
                    .value(BigDecimal.ONE)
                    .build()));

    final Response searchResponse = target(UriVariable.ACCOUNTS_PATH)
            .request()
            .get();

    assertEquals(searchResponse.getStatus(), HttpStatus.OK_200);

    final List<AccountDto> accounts = searchResponse
            .readEntity(ACCOUNTS_LIST_GENERIC_TYPE);

    assertNotNull(accounts);
    assertEquals(accounts.size(), 10);
  }

  @Test
  public void update() {
    final BigDecimal value = new BigDecimal(100);

    final AccountDto account = save(AccountDto.builder()
            .username("Petr")
            .currency("RUR")
            .value(value)
            .build());

    final Response response = target(UriVariable.ACCOUNTS_PATH + "/" + account.getId())
            .request()
            .post(Entity.entity(AccountDto.builder()
                    .username("Petr")
                    .currency("RUR")
                    .value(BigDecimal.ONE)
                    .build(), MediaType.APPLICATION_JSON));

    assertEquals(response.getStatus(), HttpStatus.OK_200);

    final AccountDto updated = response.readEntity(AccountDto.class);

    assertEquals(updated.getId(), account.getId());
    assertEquals(updated.getUsername(), account.getUsername());
    assertEquals(updated.getCurrency(), account.getCurrency());
    assertEquals(updated.getValue(), BigDecimal.ONE);
  }

  @Test
  public void transfer() {
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

    assertEquals(response.getStatus(), HttpStatus.OK_200);

    final AccountDto accountSourceUpdated = get(accountSource.getId());
    final AccountDto accountDestinationUpdated = get(accountDestination.getId());

    assertNotNull(accountSourceUpdated.getId());
    assertEquals(accountSourceUpdated.getId(), accountSource.getId());
    assertEquals(accountSourceUpdated.getUsername(), accountSource.getUsername());
    assertEquals(accountSourceUpdated.getCurrency(), accountSource.getCurrency());
    assertEquals(accountSourceUpdated.getValue(), accountSource.getValue().subtract(value));

    assertNotNull(accountDestinationUpdated.getId());
    assertEquals(accountDestinationUpdated.getId(), accountDestination.getId());
    assertEquals(accountDestinationUpdated.getUsername(), accountDestination.getUsername());
    assertEquals(accountDestinationUpdated.getCurrency(), accountDestination.getCurrency());
    assertEquals(accountDestinationUpdated.getValue(), accountDestination.getValue().add(value));
  }

  private AccountDto get(final String id) {
    final Response searchResponse = target(UriVariable.ACCOUNTS_PATH + "/" + id)
            .request()
            .get();

    assertEquals(searchResponse.getStatus(), HttpStatus.OK_200);

    return searchResponse
            .readEntity(AccountDto.class);
  }

  private AccountDto save(final AccountDto account) {
    final Response response = target(UriVariable.ACCOUNTS_PATH)
            .request()
            .post(Entity.entity(account, MediaType.APPLICATION_JSON));

    assertEquals(response.getStatus(), HttpStatus.CREATED_201);

    return response
            .readEntity(AccountDto.class);
  }

  @Test
  public void incompatibleCurrencyOfSourceAndDestination() {
    final BigDecimal value = new BigDecimal(100);

    final AccountDto accountSource = save(AccountDto.builder()
            .username("Petr")
            .currency("EUR")
            .value(value)
            .build());

    final AccountDto accountDestination = save(AccountDto.builder()
            .username("Ivan")
            .currency("RUR")
            .value(value)
            .build());

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

    assertEquals(response.getStatus(), HttpStatus.PRECONDITION_FAILED_412);

    final ErrorResponseDto error = response
            .readEntity(ErrorResponseDto.class);

    assertNotNull(error);
    assertEquals(error.getMessage(), ErrorCode.SOURCE_AND_DESTINATION_INCOMPATIBLE_CURRENCY.name());
  }

  @Test
  public void incompatibleCurrencyOfAccountAndTransferIntention() {
    final BigDecimal value = new BigDecimal(100);

    final AccountDto accountSource = save(AccountDto.builder()
            .username("Petr")
            .currency("EUR")
            .value(value)
            .build());

    final AccountDto accountDestination = save(AccountDto.builder()
            .username("Ivan")
            .currency("EUR")
            .value(value)
            .build());

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

    assertEquals(response.getStatus(), HttpStatus.PRECONDITION_FAILED_412);

    final ErrorResponseDto error = response
            .readEntity(ErrorResponseDto.class);

    assertNotNull(error);
    assertEquals(error.getMessage(), ErrorCode.SOURCE_AND_DESTINATION_INCOMPATIBLE_CURRENCY.name());
  }

  @Test
  public void insufficientFundsBeforeTransfer() {
    final BigDecimal value = new BigDecimal(100);

    final AccountDto accountSource = save(AccountDto.builder()
            .username("Petr")
            .currency("RUR")
            .value(new BigDecimal(-50))
            .build());

    final AccountDto accountDestination = save(AccountDto.builder()
            .username("Ivan")
            .currency("RUR")
            .value(value)
            .build());

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

    assertEquals(response.getStatus(), HttpStatus.PRECONDITION_FAILED_412);

    final ErrorResponseDto error = response
            .readEntity(ErrorResponseDto.class);

    assertNotNull(error);
    assertEquals(error.getMessage(), ErrorCode.SOURCE_INSUFFICIENT_FUNDS.name());
  }

  @Test
  public void insufficientFundsAfterTransfer() {
    final BigDecimal value = new BigDecimal(100);

    final AccountDto accountSource = save(AccountDto.builder()
            .username("Petr")
            .currency("RUR")
            .value(new BigDecimal(50))
            .build());

    final AccountDto accountDestination = save(AccountDto.builder()
            .username("Ivan")
            .currency("RUR")
            .value(value)
            .build());

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

    assertEquals(response.getStatus(), HttpStatus.PRECONDITION_FAILED_412);

    final ErrorResponseDto error = response
            .readEntity(ErrorResponseDto.class);

    assertNotNull(error);
    assertEquals(error.getMessage(), ErrorCode.SOURCE_INSUFFICIENT_FUNDS.name());
  }

  @Test
  public void transferAmountZeroInvalid() {
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

    final Response response = target(UriVariable.TRANSFER_PATH)
            .request()
            .post(Entity.entity(TransferDto.builder()
                    .accountSourceId(accountSource.getId())
                    .accountDestinationId(accountDestination.getId())
                    .amount(AmountDto.builder()
                            .currency("RUR")
                            .value(new BigDecimal(0))
                            .build())
                    .build(), MediaType.APPLICATION_JSON));

    assertEquals(response.getStatus(), HttpStatus.PRECONDITION_FAILED_412);

    final ErrorResponseDto error = response
            .readEntity(ErrorResponseDto.class);

    assertNotNull(error);
    assertEquals(error.getMessage(), ErrorCode.TRANSFER_AMOUNT_INVALID.name());
  }

  @Test
  public void transferAmountNegativeInvalid() {
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

    final Response response = target(UriVariable.TRANSFER_PATH)
            .request()
            .post(Entity.entity(TransferDto.builder()
                    .accountSourceId(accountSource.getId())
                    .accountDestinationId(accountDestination.getId())
                    .amount(AmountDto.builder()
                            .currency("RUR")
                            .value(new BigDecimal(-10))
                            .build())
                    .build(), MediaType.APPLICATION_JSON));

    assertEquals(response.getStatus(), HttpStatus.PRECONDITION_FAILED_412);

    final ErrorResponseDto error = response
            .readEntity(ErrorResponseDto.class);

    assertNotNull(error);
    assertEquals(error.getMessage(), ErrorCode.TRANSFER_AMOUNT_INVALID.name());
  }
}
