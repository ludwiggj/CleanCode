package clean.code.chapter14.refactored.step.by.step;

import java.util.*;

import static clean.code.chapter14.refactored.step.by.step.ArgsException.ErrorCode.*;

public class Args {
  private Map<Character, ArgumentMarshaller> marshallers =
      new HashMap<Character, ArgumentMarshaller>();
  private Set<Character> argsFound = new HashSet<Character>();
  private int noOfArguments = 0;
  private Iterator<String> currentArgument;

  public Args(String schema, String[] args) throws ArgsException {
    parseSchema(schema);
    parseArgumentStrings(Arrays.asList(args));
  }

  private void parseSchema(String schema) throws ArgsException {
    for (String element : schema.split(",")) {
      if (element.length() > 0) {
        parseSchemaElement(element.trim());
      }
    }
  }

  private void parseSchemaElement(String element) throws ArgsException {
    char elementId = element.charAt(0);
    String elementTail = element.substring(1);
    validateSchemaElementId(elementId);

    if (elementTail.length() == 0)
      marshallers.put(elementId, new BooleanArgumentMarshaller());
    else if (elementTail.equals("*"))
      marshallers.put(elementId, new StringArgumentMarshaller());
    else if (elementTail.equals("#")) {
      marshallers.put(elementId, new IntegerArgumentMarshaller());
    } else if (elementTail.equals("##")) {
      marshallers.put(elementId, new DoubleArgumentMarshaller());
    } else {
      throw new ArgsException(INVALID_ARGUMENT_FORMAT, elementId, elementTail);
    }
  }

  private void validateSchemaElementId(char elementId) throws ArgsException {
    if (!Character.isLetter(elementId)) {
      throw new ArgsException(INVALID_ARGUMENT_NAME, elementId);
    }
  }

  private void parseArgumentStrings(List<String> argsList) throws ArgsException {
    for (currentArgument = argsList.iterator(); currentArgument.hasNext(); ) {
      String arg = currentArgument.next();
      if (arg.startsWith("-")) {
        noOfArguments = ++noOfArguments;
        parseArgumentCharacters(arg.substring(1));
      } else {
        break;
      }
    }
    if ((marshallers.size() > 0) && (noOfArguments == 0)) {
      throw new ArgsException(INVALID_ARGUMENT_FORMAT, '-');
    }
  }

  private void parseArgumentCharacters(String arg) throws ArgsException {
    for (int i = 0; i < arg.length(); i++) {
      parseArgumentCharacter(arg.charAt(i));
    }
  }

  private void parseArgumentCharacter(final char argChar) throws ArgsException {
    ArgumentMarshaller m = marshallers.get(argChar);
    if (m == null) {
      throw new ArgsException(UNEXPECTED_ARGUMENT, argChar);
    }
    argsFound.add(argChar);
    try {
    m.set(currentArgument);
    } catch (ArgsException e) {
      e.setErrorArgumentId(argChar);
      throw e;
    }
  }

  public int cardinality() {
    return argsFound.size();
  }

  public String getString(char arg) {
    return StringArgumentMarshaller.getValue(marshallers.get(arg));
  }

  public int getInt(char arg) {
    return IntegerArgumentMarshaller.getValue(marshallers.get(arg));
  }

  public boolean getBoolean(char arg) {
    return BooleanArgumentMarshaller.getValue(marshallers.get(arg));
  }

  public double getDouble(char arg) {
    return DoubleArgumentMarshaller.getValue(marshallers.get(arg));
  }

  public boolean has(char arg) {
    return argsFound.contains(arg);
  }
}