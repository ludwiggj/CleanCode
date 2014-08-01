package clean.code.chapter14.refactored.step.by.step;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class StringArgumentMarshaller implements ArgumentMarshaller {
  private String stringValue;

  @Override
  public void set(Iterator<String> currentArgument, char argChar) throws ArgsException {
    try {
      stringValue = currentArgument.next();
    } catch (NoSuchElementException e) {
      throw new ArgsException(ArgsException.ErrorCode.MISSING_STRING, argChar);
    }
  }

  @Override
  public Object get() {
    return (stringValue == null) ? "" : stringValue;
  }
}