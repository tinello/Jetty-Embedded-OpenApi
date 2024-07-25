package ar.com.tinello.api.core.info.domain;

public final class ServiceInfo {
  
  private final String apiName;
  private final String apiVersion;
  private final boolean apiHealthy;

  public ServiceInfo(final String apiName, final String apiVersion, final boolean apiHealthy) {
    this.apiName = apiName;
    this.apiVersion = apiVersion;
    this.apiHealthy = apiHealthy;
  }

  public final String getApiName() {
    return this.apiName;
  }
  public final String getApiVersion() {
    return this.apiVersion;
  }
  public final boolean getApiHealthy() {
    return this.apiHealthy;
  }

}
