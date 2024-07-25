package ar.com.tinello.api.web;

import org.eclipse.jetty.server.Request;

public interface Operation {

  String getId();

  String execute(final Request req);
}
