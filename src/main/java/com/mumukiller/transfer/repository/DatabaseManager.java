package com.mumukiller.transfer.repository;

import com.querydsl.sql.SQLQueryFactory;

import javax.sql.DataSource;
import java.util.function.Consumer;

public interface DatabaseManager {

  SQLQueryFactory getQueryFactory();

  void doTransaction(final Consumer<SQLQueryFactory> sqlQueryFactoryConsumer);
}
