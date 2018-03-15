package com.mumukiller.transfer.controller;

import com.mumukiller.transfer.dto.AccountDto;
import com.mumukiller.transfer.service.AccountService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path(UriVariable.ACCOUNTS_PATH)
public class AccountController {

  private final AccountService accountService;

  @Inject
  public AccountController(final AccountService accountService) {
    this.accountService = accountService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<AccountDto> findAll() {
    return accountService.findAll();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public AccountDto findOne(@PathParam("id") final String id) {
    return accountService.find(id);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response save(final AccountDto account) {
    return Response.status(Response.Status.CREATED)
            .entity(accountService.save(account))
            .build();
  }

  @POST
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(final AccountDto account, @PathParam("id") final String id) {
    return Response.status(Response.Status.OK)
            .entity(accountService.update(id, account))
            .build();
  }
}
