package ar.com.tinello.api.web.operations;

import org.eclipse.jetty.server.Request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tinello.api.core.Provider;
import ar.com.tinello.api.web.Operation;
import ar.com.tinello.api.web.ServerException;

public final class ServiceInfo implements Operation {

  private final Provider provider;

  public ServiceInfo(final Provider provider) {
    this.provider = provider;
  }

  @Override
  public final String getId() {
    return "service_info";
  }

  @Override
  public final String execute(final Request req) {

    final var objectMapper = new ObjectMapper();
    final var objectNode = objectMapper.createObjectNode();
    
    try {
      final var response = provider.getServiceInfo().execute();

      objectNode.put("name", response.getApiName());
      objectNode.put("version", response.getApiVersion());
      objectNode.put("healthy", response.getApiHealthy());

      return objectMapper.writeValueAsString(objectNode);
    } catch (JsonProcessingException e) {
      throw new ServerException(e);
    }
  }
  
}
