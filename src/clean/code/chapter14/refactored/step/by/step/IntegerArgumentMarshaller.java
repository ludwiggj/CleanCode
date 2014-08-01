package clean.code.chapter14.refactored.step.by.step;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IntegerArgumentMarshaller implements ArgumentMarshaller {
  private Integer intValue;

  @Override
  public void set(Iterator<String> currentArgument, char argChar) throws ArgsException {
    String parameter = null;
    try {
      parameter = currentArgument.next();
      intValue = new Integer(parameter);
    } catch (NoSuchElementException e) {
      throw new ArgsException(ArgsException.ErrorCode.MISSING_INTEGER, argChar);
    } catch (NumberFormatException e) {
      throw new ArgsException(ArgsException.ErrorCode.INVALID_INTEGER, argChar, parameter);
    }
  }

  @Override
  public Object get() {
    return (intValue == null) ? 0 : intValue;
  }
}