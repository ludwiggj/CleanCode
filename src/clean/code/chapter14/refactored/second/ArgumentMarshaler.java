package clean.code.chapter14.refactored.second;

import java.util.Iterator;

public interface ArgumentMarshaler {
   void set(Iterator<String> currentArgument) throws ArgsException;
   Object get();
}
