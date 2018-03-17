package com.mumukiller.transfer.exception.handler;

import com.mumukiller.transfer.exception.ErrorCode;
import com.querydsl.core.QueryException;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbc.JdbcSQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class QueryExceptionHandler extends BaseExceptionHandler<QueryException> {
  public Response toResponse(final QueryException e) {
    log.error(e.getMessage());
    if (e.getCause() instanceof JdbcSQLException) {
      final JdbcSQLException cause = (JdbcSQLException) e.getCause();
      log.error("Exception cause is JdbcSQLException:", cause);
      return build(Response.Status.INTERNAL_SERVER_ERROR, ErrorCode.TRANSFER_ALREADY_IN_PROGRESS.name());
    }

    return build(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
  }
}
