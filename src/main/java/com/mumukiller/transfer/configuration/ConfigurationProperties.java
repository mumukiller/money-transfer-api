package com.mumukiller.transfer.configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Getter
public class ConfigurationProperties {

  private static final String RESOURCE_NAME = "jdbc.properties";

  private final Properties properties;

  public ConfigurationProperties() {
    this.properties = loadProperties(RESOURCE_NAME);
  }

  private Properties loadProperties(final String resourceName){
    final Properties properties = new Properties();

    final ClassLoader loader = Thread.currentThread().getContextClassLoader();
    try(final InputStream resourceStream = loader.getResourceAsStream(RESOURCE_NAME)) {
      properties.load(resourceStream);
    } catch (IOException e){
      log.error("Unable to load properties from {}", RESOURCE_NAME);
    }

    return properties;
  }
}
