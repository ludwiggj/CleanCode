package clean.code.chapter14.refactored.step.by.step;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BooleanArgumentMarshaller implements ArgumentMarshaller {
  private boolean booleanValue = false;

  @Override
  public void set(Iterator<String> currentArgument, char argChar) throws ArgsException {
    try {
      booleanValue = new Boolean(currentArgument.next());
    } catch (NoSuchElementException e) {
      throw new ArgsException(ArgsException.ErrorCode.MISSING_BOOLEAN, argChar);
    }
  }

  @Override
  public Object get() {
    return booleanValue;
  }
}