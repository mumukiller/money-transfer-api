package com.mumukiller.transfer;


import com.mumukiller.transfer.configuration.ApplicationBinder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.ws.rs.ApplicationPath;
import java.util.logging.Level;

@ApplicationPath("/")
public class MoneyTransferApplication extends ResourceConfig {

  public MoneyTransferApplication() {
    packages("com.mumukiller.transfer");

    property(LoggingFeature.LOGGING_FEATURE_VERBOSITY, LoggingFeature.Verbosity.PAYLOAD_ANY);
    property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, Level.FINEST.getName());

    property(ServerProperties.TRACING, "ALL");
    property(ServerProperties.TRACING_THRESHOLD, "VERBOSE");

    register(new ApplicationBinder());
  }

  public static void main(String[] args) throws Exception {
    final ServletHolder servlet = new ServletHolder(new ServletContainer(new MoneyTransferApplication()));
    final Server server = new Server(8080);
    final ServletContextHandler context = new ServletContextHandler(server, "/money-transfers-api");
    context.addServlet(servlet, "/*");

    try {
      server.start();
      server.join();
    } finally {
      server.destroy();
    }
  }
}