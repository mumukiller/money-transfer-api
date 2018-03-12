package com.mumukiller.transfer.service;

import com.mumukiller.transfer.dto.AccountDto;
import com.mumukiller.transfer.dto.TransferDto;
import com.mumukiller.transfer.entity.Account;
import com.mumukiller.transfer.exception.ErrorCode;
import com.mumukiller.transfer.repository.AccountRepository;
import com.mumukiller.transfer.repository.AccountRepositoryImpl;
import com.mumukiller.transfer.validation.TransferValidator;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final TransferValidator transferValidator;

  public AccountServiceImpl() {
    this.accountRepository = new AccountRepositoryImpl();
    this.transferValidator = new TransferValidator();
  }

  @Override
  public List<AccountDto> findAll() {
    return accountRepository.findAll()
            .stream()
            .map(this::convert)
            .collect(toList());
  }

  @Override
  public AccountDto find(final String id) {
    return Optional.ofNullable(accountRepository.find(id))
            .map(this::convert)
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.name()));
  }

  @Override
  public AccountDto save(final AccountDto id) {
    return convert(accountRepository.save(id));
  }

  private AccountDto convert(final Account account) {
    return AccountDto.builder()
            .id(account.getId())
            .username(account.getUsername())
            .currency(account.getCurrency())
            .value(account.getValue())
            .build();
  }

  @Override
  public AccountDto update(final String id, final AccountDto account) {
    return convert(accountRepository.update(id, account));
  }

  @Override
  public void transfer(final TransferDto transaction) {
    accountRepository.transfer(transaction, transferValidator::validate);
  }
}
