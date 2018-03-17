package com.mumukiller.transfer.configuration;

import com.mumukiller.transfer.repository.AccountRepository;
import com.mumukiller.transfer.repository.AccountRepositoryImpl;
import com.mumukiller.transfer.repository.DatabaseManager;
import com.mumukiller.transfer.repository.DatabaseManagerImpl;
import com.mumukiller.transfer.service.AccountService;
import com.mumukiller.transfer.service.AccountServiceImpl;
import com.mumukiller.transfer.validation.TransferValidator;
import lombok.Getter;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

public class TestApplicationBinder extends AbstractBinder {

  @Getter
  private final TransferValidator mockTransferValidator = Mockito.mock(TransferValidator.class);

  @Override
  protected void configure() {
    bind(AccountServiceImpl.class).to(AccountService.class);
    bind(AccountRepositoryImpl.class).to(AccountRepository.class);
    bind(mockTransferValidator).to(TransferValidator.class);
    bind(DatabaseManagerImpl.class).to(DatabaseManager.class);
  }
}
