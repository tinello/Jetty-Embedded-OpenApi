package ar.com.tinello.api.web.operations;

import org.eclipse.jetty.server.Request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tinello.api.core.Provider;
import ar.com.tinello.api.web.Operation;
import ar.com.tinello.api.web.ServerException;

public class ServiceInfo implements Operation {

  @Override
  public String getId() {
    return "service_info";
  }

  @Override
  public String execute(Request req, Provider provider) {

    ObjectMapper objectMapper = new ObjectMapper();

    final var obj = objectMapper.createObjectNode();
    
    
    try {

      final var response = provider.getServiceInfo().execute();

      obj.put("name", response.getApiName());
      obj.put("version", response.getApiVersion());
      obj.put("healthy", response.getApiHealthy());

      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new ServerException(e);
    }
  }
  
}
