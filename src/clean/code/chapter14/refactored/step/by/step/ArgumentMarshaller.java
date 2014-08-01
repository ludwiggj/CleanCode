package clean.code.chapter14.refactored.step.by.step;

import java.util.Iterator;

public interface ArgumentMarshaller {
  void set(Iterator<String> currentArgument, char argChar) throws ArgsException;

  Object get();
}