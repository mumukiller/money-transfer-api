package com.mumukiller.transfer.repository;

import com.mumukiller.transfer.dto.AccountDto;
import com.mumukiller.transfer.dto.TransferDto;
import com.mumukiller.transfer.entity.Account;
import com.mumukiller.transfer.service.TransferOperationHolder;
import com.querydsl.sql.SQLQueryFactory;

import java.sql.Connection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface AccountRepository {

  List<Account> findAll();

  Account find(final String id);

  Account save(final AccountDto account);

  void transfer(final TransferDto transaction, final Consumer<TransferOperationHolder> validationConsumer);

  Account update(final String id, final AccountDto account);
}
