package com.mumukiller.transfer.service;

import com.mumukiller.transfer.dto.AccountDto;
import com.mumukiller.transfer.dto.TransferDto;
import com.mumukiller.transfer.entity.Account;

import java.util.List;

public interface AccountService {

  List<AccountDto> findAll();

  AccountDto find(final String id);

  AccountDto save(final AccountDto id);

  AccountDto update(final String id, final AccountDto account);

  void transfer(final TransferDto transaction);
}
