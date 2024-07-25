package ar.com.tinello.api.web;

import java.util.HashMap;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.openapi4j.core.exception.ResolutionException;
import org.openapi4j.core.validation.ValidationException;
import org.openapi4j.operation.validator.validation.RequestValidator;
import org.openapi4j.parser.OpenApi3Parser;
import org.openapi4j.parser.model.v3.OpenApi3;

import ar.com.tinello.api.core.Environment;
import ar.com.tinello.api.core.Provider;
import ar.com.tinello.api.web.operations.ServiceInfo;

public class MainJetty {

    private MainJetty(){}

    public static void start() {
        final var jetty = new Server();
        final var connector = new ServerConnector(jetty);
        connector.setPort(8080);
        jetty.setConnectors(new Connector[] {connector});


        final var classLoader = MainJetty.class.getClassLoader();
        final var resource = classLoader.getResource("contract.yaml");

        OpenApi3 api;
        try {
        api = new OpenApi3Parser().parse(resource, false);
        
        } catch (ResolutionException | ValidationException e) {
            throw new ServerException(e);
        }
        
        final var operations = new HashMap<String, Operation>();
        final var serviceInfo = new ServiceInfo();
        operations.put(serviceInfo.getId(), serviceInfo);

        jetty.setHandler(new MainHandler(new RequestValidator(api), new Provider(new Environment()), operations));

        
        try {
            jetty.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jetty.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
