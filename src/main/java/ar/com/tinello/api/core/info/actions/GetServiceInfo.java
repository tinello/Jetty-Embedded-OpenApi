package ar.com.tinello.api.core.info.actions;

import ar.com.tinello.api.core.info.domain.ServiceInfo;
import ar.com.tinello.api.core.info.domain.ServiceInfoRepo;

public final class GetServiceInfo {
  
  private final ServiceInfoRepo repo;

  public GetServiceInfo(final ServiceInfoRepo repo) {
    this.repo = repo;
  }

  public final ServiceInfo execute() {
    return repo.get();
  }

}
