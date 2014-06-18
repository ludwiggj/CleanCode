package clean.code.chapter14.refactored.second;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static clean.code.chapter14.refactored.second.ArgsException.ErrorCode.INVALID_INTEGER;
import static clean.code.chapter14.refactored.second.ArgsException.ErrorCode.MISSING_INTEGER;

public class IntegerArgumentMarshaler implements ArgumentMarshaler {
  private int intValue = 0;

  public void set(Iterator<String> currentArgument) throws ArgsException {
    String parameter = null;
    try {
      parameter = currentArgument.next();
      intValue = Integer.parseInt(parameter);
    } catch (NoSuchElementException e) {
      throw new ArgsException(MISSING_INTEGER);
    } catch (NumberFormatException e) {
      throw new ArgsException(INVALID_INTEGER, parameter);
    }
  }

  public Object get() {
    return intValue;
  }
}