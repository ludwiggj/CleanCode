package clean.code.chapter14.refactored.second;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static clean.code.chapter14.refactored.second.ArgsException.ErrorCode.INVALID_BOOLEAN;
import static clean.code.chapter14.refactored.second.ArgsException.ErrorCode.MISSING_BOOLEAN;

public class BooleanArgumentMarshaler implements ArgumentMarshaler {
  private boolean booleanValue = false;

  public void set(Iterator<String> currentArgument) throws ArgsException {
    String parameter = null;
    try {
      parameter = currentArgument.next();
      booleanValue = Boolean.parseBoolean(parameter);
    } catch (NoSuchElementException e) {
      throw new ArgsException(MISSING_BOOLEAN);
    } catch (NumberFormatException e) {
      throw new ArgsException(INVALID_BOOLEAN, parameter);
    }
  }

  public Object get() {
    return booleanValue;
  }
}