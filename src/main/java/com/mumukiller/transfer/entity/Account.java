package com.mumukiller.transfer.entity;

import lombok.Getter;
import lombok.Setter;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@PersistenceCapable(table = "account", schema = "public")
public class Account {
  @PrimaryKey
  //@Persistent
  private String id = UUID.randomUUID().toString();
  @Column
  //@Persistent
  private String username;
  @Column
  //@Persistent
  private BigDecimal value;
  @Column
  //@Persistent
  private String currency;
}
