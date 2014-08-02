package clean.code.chapter14.refactored.step.by.step;

public class ArgsException extends Exception {
  private char errorArgumentId = '\0';

  private String errorParameter = "TILT";
  private ErrorCode errorCode = ErrorCode.OK;
  public ArgsException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  public ArgsException(ErrorCode errorCode, char errorArgumentId) {
    this(errorCode);
    this.errorArgumentId = errorArgumentId;
  }

  public ArgsException(ErrorCode errorCode, String errorParameter) {
    this(errorCode);
    this.errorParameter = errorParameter;
  }

  public ArgsException(ErrorCode errorCode, char errorArgumentId, String errorParameter) {
    this(errorCode, errorArgumentId);
    this.errorParameter = errorParameter;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public void setErrorArgumentId(char errorArgumentId) {
    this.errorArgumentId = errorArgumentId;
  }

  public char getErrorArgumentId() {
    return errorArgumentId;
  }

  public String getErrorParameter() {
    return errorParameter;
  }

  public enum ErrorCode {
    OK, MISSING_STRING, MISSING_INTEGER, MISSING_BOOLEAN,
    INVALID_INTEGER, MISSING_DOUBLE, INVALID_DOUBLE,
    UNEXPECTED_ARGUMENT, INVALID_ARGUMENT_NAME, INVALID_ARGUMENT_FORMAT;
  }

  public String errorMessage() throws Exception {
    switch (errorCode) {
      case OK:
        throw new Exception("TILT: Should not get here.");
      case UNEXPECTED_ARGUMENT:
        return String.format("Argument -%s unexpected.", errorArgumentId);
      case INVALID_ARGUMENT_NAME:
        return String.format("Bad character:%s in Args format", errorArgumentId);
      case MISSING_STRING:
        return String.format("Could not find string parameter for -%c.", errorArgumentId);
      case MISSING_INTEGER:
        return String.format("Could not find integer parameter for -%c.", errorArgumentId);
      case INVALID_INTEGER:
        return String.format("Argument -%c expects an integer but was '%s'.", errorArgumentId, errorParameter);
      case MISSING_BOOLEAN:
        return String.format("Could not find boolean parameter for -%c.", errorArgumentId);
      case MISSING_DOUBLE:
        return String.format("Could not find double parameter for -%c.", errorArgumentId);
      case INVALID_DOUBLE:
        return String.format("Argument -%c expects a double but was '%s'.", errorArgumentId, errorParameter);
    }
    return "";
  }
}