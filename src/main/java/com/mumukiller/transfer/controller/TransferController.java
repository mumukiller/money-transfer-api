package com.mumukiller.transfer.controller;

import com.mumukiller.transfer.dto.TransferDto;
import com.mumukiller.transfer.service.AccountService;
import com.mumukiller.transfer.service.AccountServiceImpl;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(UriVariable.TRANSFER_PATH)
public class TransferController {

  private final AccountService accountService;

  @Inject
  public TransferController(final AccountService accountService) {
    this.accountService = accountService;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response transfer(final TransferDto transfer) {
    accountService.transfer(transfer);

    return Response.status(Response.Status.OK)
            .build();
  }
}

