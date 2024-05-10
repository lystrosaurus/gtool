package io.github.lystrosaurus.exceptions;

/**
 * Created by IntelliJ IDEA.
 *
 * @author eric 2024/5/9 17:49
 */
public class GException extends RuntimeException {

  public GException() {
    super();
  }

  public GException(String message) {
    super(message);
  }

  public GException(Throwable e) {
    super(e);
  }
}
