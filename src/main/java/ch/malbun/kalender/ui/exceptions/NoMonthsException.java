package ch.malbun.kalender.ui.exceptions;

public class NoMonthsException extends RuntimeException {
  public NoMonthsException() {
    super();
  }

  public NoMonthsException(String message) {
    super(message);
  }

  public NoMonthsException(Throwable throwable) {
    super(throwable);
  }

  public NoMonthsException(String messgae, Throwable throwable) {
    super(messgae, throwable);
  }
}
