package clean.code.chapter14.refactored.step.by.step;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoubleArgumentMarshaller implements ArgumentMarshaller {
  private double doubleValue = 0;

  @Override
  public void set(Iterator<String> currentArgument, char argChar) throws ArgsException {
    String parameter = null;
    try {
      parameter = currentArgument.next();
      doubleValue = Double.parseDouble(parameter);
    } catch (NoSuchElementException e) {
      throw new ArgsException(ArgsException.ErrorCode.MISSING_DOUBLE, argChar);
    } catch (NumberFormatException e) {
      throw new ArgsException(ArgsException.ErrorCode.INVALID_DOUBLE, argChar, parameter);
    }
  }

  @Override
  public Object get() {
    return doubleValue;
  }
}