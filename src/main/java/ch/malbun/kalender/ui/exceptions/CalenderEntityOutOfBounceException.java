package ch.malbun.kalender.ui.exceptions;

public class CalenderEntityOutOfBounceException extends Exception {
  public CalenderEntityOutOfBounceException() {
    super();
  }
  public CalenderEntityOutOfBounceException(String message) {
    super(message);
  }

  public CalenderEntityOutOfBounceException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public CalenderEntityOutOfBounceException(Throwable throwable) {
    super(throwable);
  }
}
