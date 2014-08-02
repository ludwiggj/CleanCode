package clean.code.chapter14.refactored.step.by.step;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IntegerArgumentMarshaller implements ArgumentMarshaller {
  private Integer intValue;

  @Override
  public void set(Iterator<String> currentArgument) throws ArgsException {
    String parameter = null;
    try {
      parameter = currentArgument.next();
      intValue = new Integer(parameter);
    } catch (NoSuchElementException e) {
      throw new ArgsException(ArgsException.ErrorCode.MISSING_INTEGER);
    } catch (NumberFormatException e) {
      throw new ArgsException(ArgsException.ErrorCode.INVALID_INTEGER, parameter);
    }
  }

  public static int getValue(ArgumentMarshaller am) {
    if ((am != null) && am instanceof IntegerArgumentMarshaller) {
      return ((IntegerArgumentMarshaller) am).intValue;
    } else {
      return 0;
    }
  }
}