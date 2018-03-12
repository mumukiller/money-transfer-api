package com.mumukiller.transfer.repository;

import com.google.common.collect.ImmutableList;
import com.mumukiller.transfer.dto.AccountDto;
import com.mumukiller.transfer.dto.TransferDto;
import com.mumukiller.transfer.entity.Account;
import com.mumukiller.transfer.entity.QAccount;
import com.mumukiller.transfer.exception.ApplicationException;
import com.mumukiller.transfer.service.TransferOperationHolder;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLUpdateClause;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Getter
public class AccountRepositoryImpl implements AccountRepository {

  private final DatabaseManager databaseManager;

  public AccountRepositoryImpl() {
    this.databaseManager = new DatabaseManagerImpl();
  }

  @Override
  public List<Account> findAll() {
    final SQLQueryFactory queryFactory = getDatabaseManager().getQueryFactory();
    return queryFactory.select(QAccount.account)
            .from(QAccount.account)
            .limit(10)
            .fetch();
  }

  @Override
  public Account find(final String id) {
    final SQLQueryFactory queryFactory = getDatabaseManager().getQueryFactory();
    return queryFactory.select(QAccount.account)
            .from(QAccount.account)
            .where(QAccount.account.id.eq(id))
            .fetchOne();
  }

  @Override
  public Account save(final AccountDto account) {
    final SQLQueryFactory queryFactory = getDatabaseManager().getQueryFactory();
    final String id = UUID.randomUUID().toString();
    queryFactory.insert(QAccount.account)
            .set(QAccount.account.username, account.getUsername())
            .set(QAccount.account.value, account.getValue())
            .set(QAccount.account.currency, account.getCurrency())
            .set(QAccount.account.id, id)
            .execute();

    return find(id);
  }

  @Override
  public Account update(final String id, final AccountDto account) {
    final SQLQueryFactory queryFactory = getDatabaseManager().getQueryFactory();
    queryFactory.update(QAccount.account)
            .set(QAccount.account.value, account.getValue())
            .where(QAccount.account.id.eq(id))
            .execute();

    return find(id);
  }

  @Override
  public void transfer(final TransferDto transaction, final Consumer<TransferOperationHolder> validationConsumer) {
    try {
      getDatabaseManager().doTransaction(factory -> doTransfer(factory, transaction, validationConsumer));
    } catch (Exception e){
      if (e.getCause() instanceof ApplicationException){
        throw (ApplicationException)e.getCause();
      }

      log.error("Transactions failed. {}", e.getMessage());
      throw e;
    }
  }

  private void doTransfer(final SQLQueryFactory queryFactory,
                          final TransferDto transaction,
                          final Consumer<TransferOperationHolder> validationConsumer) {
    final List<String> idsToTransfer =
            ImmutableList.of(transaction.getAccountSourceId(), transaction.getAccountDestinationId());

    final Map<String, Account> accounts = queryFactory.select(QAccount.account)
            .forUpdate()
            .from(QAccount.account)
            .where(QAccount.account.id.in(idsToTransfer))
            .fetch()
            .stream()
            .collect(toMap(Account::getId, Function.identity()));

    final Account source = accounts.get(transaction.getAccountSourceId());
    final Account destination = accounts.get(transaction.getAccountDestinationId());

    validationConsumer.accept(TransferOperationHolder.builder()
            .transaction(transaction)
            .source(source)
            .destination(destination)
            .build());

    final BigDecimal sourceFinalValue = source.getValue()
            .subtract(transaction.getAmount().getValue());
    final BigDecimal destinationFinalValue = destination.getValue()
            .add(transaction.getAmount().getValue());

    final SQLUpdateClause updateClause = queryFactory.update(QAccount.account)
            .set(QAccount.account.value, sourceFinalValue).where(QAccount.account.id.eq(transaction.getAccountSourceId()))
            .addBatch()
            .set(QAccount.account.value, destinationFinalValue).where(QAccount.account.id.eq(transaction.getAccountDestinationId()))
            .addBatch();

    updateClause.execute();
  }
}
