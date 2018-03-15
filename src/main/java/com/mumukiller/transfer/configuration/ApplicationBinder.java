package com.mumukiller.transfer.configuration;

import com.mumukiller.transfer.repository.AccountRepository;
import com.mumukiller.transfer.repository.AccountRepositoryImpl;
import com.mumukiller.transfer.repository.DatabaseManager;
import com.mumukiller.transfer.repository.DatabaseManagerImpl;
import com.mumukiller.transfer.service.AccountService;
import com.mumukiller.transfer.service.AccountServiceImpl;
import com.mumukiller.transfer.validation.TransferValidator;
import com.mumukiller.transfer.validation.TransferValidatorImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApplicationBinder extends AbstractBinder {
  @Override
  protected void configure() {
    bind(AccountServiceImpl.class).to(AccountService.class);
    bind(AccountRepositoryImpl.class).to(AccountRepository.class);
    bind(TransferValidatorImpl.class).to(TransferValidator.class);
    bind(DatabaseManagerImpl.class).to(DatabaseManager.class);
  }
}
