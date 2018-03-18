package com.mumukiller.transfer.repository;

import com.mumukiller.transfer.configuration.ConfigurationProperties;
import com.mumukiller.transfer.exception.ApplicationException;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.types.DateTimeType;
import com.querydsl.sql.types.LocalDateType;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.inject.Provider;
import java.sql.Connection;
import java.util.Properties;
import java.util.function.Consumer;

@Slf4j
@Resource
@ManagedBean
public class DatabaseManagerImpl implements DatabaseManager {

  private final HikariDataSource dataSource;

  @Getter
  private final SQLQueryFactory queryFactory;

  public DatabaseManagerImpl() {
    final ConfigurationProperties configurationProperties = new ConfigurationProperties();
    final Properties properties = configurationProperties.getProperties();

    this.dataSource = new HikariDataSource();
    this.dataSource.setJdbcUrl(properties.getProperty("jdbc.url"));
    this.dataSource.setUsername(properties.getProperty("jdbc.user"));
    this.dataSource.setPassword(properties.getProperty("jdbc.password"));

    this.queryFactory = new SQLQueryFactory(querydslConfiguration(), this.dataSource);
  }

  @Override
  public void doTransaction(final Consumer<SQLQueryFactory> sqlQueryFactoryConsumer) {
    try (final Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      final SQLQueryFactory queryFactory = new SQLQueryFactory(querydslConfiguration(), DataSourceProvider.build(connection));
      sqlQueryFactoryConsumer.accept(queryFactory);

      connection.commit();
    } catch (Exception e) {
      if (e.getCause() instanceof ApplicationException) {
        throw (ApplicationException) e.getCause();
      }

      log.error("Transactions failed. {}", e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  private Configuration querydslConfiguration() {
    final SQLTemplates templates = H2Templates.builder()
            .printSchema()
            .build();

    final Configuration configuration = new Configuration(templates);
    configuration.register(new DateTimeType());
    configuration.register(new LocalDateType());
    return configuration;
  }


  static class DataSourceProvider implements Provider<Connection> {

    private final Connection connection;

    DataSourceProvider(final Connection connection) {
      this.connection = connection;
    }

    static DataSourceProvider build(final Connection connection) {
      return new DataSourceProvider(connection);
    }

    @Override
    public Connection get() {
      return connection;
    }
  }
}
