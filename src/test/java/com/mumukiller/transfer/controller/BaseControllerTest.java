package com.mumukiller.transfer.controller;

import com.mumukiller.transfer.dto.AccountDto;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static junit.framework.TestCase.assertEquals;

public abstract class BaseControllerTest extends JerseyTest {

  @Override
  protected Application configure() {
    //Does not work :( https://github.com/jersey/jersey/issues/3502
    set(TestProperties.LOG_TRAFFIC, true);
    set(TestProperties.DUMP_ENTITY, true);

    set(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY);
    set(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "WARNING");

    return buildApp();
  }

  public abstract Application buildApp();

  protected AccountDto get(final String id) {
    final Response searchResponse = target(UriVariable.ACCOUNTS_PATH + "/" + id)
            .request()
            .get();

    assertEquals(searchResponse.getStatus(), HttpStatus.OK_200);

    return searchResponse
            .readEntity(AccountDto.class);
  }

  protected AccountDto save(final AccountDto account) {
    final Response response = target(UriVariable.ACCOUNTS_PATH)
            .request()
            .post(Entity.entity(account, MediaType.APPLICATION_JSON));

    assertEquals(response.getStatus(), HttpStatus.CREATED_201);

    return response
            .readEntity(AccountDto.class);
  }

  protected AccountDto update(final AccountDto account) {
    final Response response = target(UriVariable.ACCOUNTS_PATH + "/" + account.getId())
            .request()
            .post(Entity.entity(account, MediaType.APPLICATION_JSON));

    assertEquals(response.getStatus(), HttpStatus.OK_200);

    return response
            .readEntity(AccountDto.class);
  }
}
