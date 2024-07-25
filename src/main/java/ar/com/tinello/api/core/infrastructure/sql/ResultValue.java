package ar.com.tinello.api.core.infrastructure.sql;

public final class ResultValue {
  
  private final Object value;

  public ResultValue(final Object value) {
    this.value = value;
  }

  public final int getInt() {
    return Integer.valueOf(value.toString());
  }

  public final String getString() {
    return value.toString();
  }

  public final boolean getBoolean() {
    return Boolean.valueOf(value.toString());
  }

}
