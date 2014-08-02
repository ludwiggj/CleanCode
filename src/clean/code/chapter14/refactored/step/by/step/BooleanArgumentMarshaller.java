package clean.code.chapter14.refactored.step.by.step;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BooleanArgumentMarshaller implements ArgumentMarshaller {
  private boolean booleanValue = false;

  @Override
  public void set(Iterator<String> currentArgument) throws ArgsException {
    try {
      booleanValue = new Boolean(currentArgument.next());
    } catch (NoSuchElementException e) {
      throw new ArgsException(ArgsException.ErrorCode.MISSING_BOOLEAN);
    }
  }

  public static boolean getValue(ArgumentMarshaller am) {
    if ((am != null) && am instanceof BooleanArgumentMarshaller) {
      return ((BooleanArgumentMarshaller) am).booleanValue;
    } else {
      return false;
    }
  }
}