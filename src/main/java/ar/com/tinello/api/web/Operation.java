package ar.com.tinello.api.web;

import org.eclipse.jetty.server.Request;

import ar.com.tinello.api.core.Provider;

public interface Operation {

  String getId();

  String execute(Request req, Provider provider);
}
