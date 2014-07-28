package clean.code.chapter14.refactored.step.by.step;

import java.text.ParseException;
import java.util.*;

public class Args {
  private String schema;
  private boolean valid = true;
  private Set<Character> unexpectedArguments = new TreeSet<Character>();
  private Map<Character, ArgumentMarshaller> marshallers = new HashMap<Character, ArgumentMarshaller>();
  private Set<Character> argsFound = new HashSet<Character>();
  private char errorArgumentId = '\0';
  private String errorParameter = "TILT";
  private ErrorCode errorCode = ErrorCode.OK;
  private int noOfArguments = 0;
  private List<String> argsList;
  private Iterator<String> currentArgument;

  private enum ErrorCode {
    OK, MISSING_STRING, MISSING_INTEGER, MISSING_BOOLEAN, INVALID_INTEGER, UNEXPECTED_ARGUMENT
  }

  public Args(String schema, String[] args) throws ParseException {
    this.schema = schema;
    this.argsList = Arrays.asList(args);
    valid = parse();
  }

  private boolean parse() throws ParseException {
    if (schema.length() == 0 && argsList.size() == 0)
      return true;
    parseSchema();
    try {
      parseArguments();
    } catch (ArgsException e) {
    }
    return valid;
  }

  private boolean parseSchema() throws ParseException {
    for (String element : schema.split(",")) {
      if (element.length() > 0) {
        String trimmedElement = element.trim();
        parseSchemaElement(trimmedElement);
      }
    }
    return true;
  }

  private void parseSchemaElement(String element) throws ParseException {
    char elementId = element.charAt(0);
    String elementTail = element.substring(1);
    validateSchemaElementId(elementId);
    if (isBooleanSchemaElement(elementTail))
      marshallers.put(elementId, new BooleanArgumentMarshaller());
    else if (isStringSchemaElement(elementTail))
      marshallers.put(elementId, new StringArgumentMarshaller());
    else if (isIntegerSchemaElement(elementTail)) {
      marshallers.put(elementId, new IntegerArgumentMarshaller());
    } else {
      throw new ParseException(
          String.format("Argument: %c has invalid format: %s.",
              elementId, elementTail), 0);
    }
  }

  private void validateSchemaElementId(char elementId) throws ParseException {
    if (!Character.isLetter(elementId)) {
      throw new ParseException(
          "Bad character:" + elementId + "in Args format: " + schema, 0);
    }
  }

  private boolean isStringSchemaElement(String elementTail) {
    return elementTail.equals("*");
  }

  private boolean isBooleanSchemaElement(String elementTail) {
    return elementTail.length() == 0;
  }

  private boolean isIntegerSchemaElement(String elementTail) {
    return elementTail.equals("#");
  }

  private boolean parseArguments() throws ArgsException {
    for (currentArgument = argsList.iterator(); currentArgument.hasNext(); ) {
      String arg = currentArgument.next();
      parseArgument(arg);
    }
    valid = valid && (noOfArguments > 0);
    return true;
  }

  private void parseArgument(String arg) throws ArgsException {
    if (arg.startsWith("-")) {
      noOfArguments = ++noOfArguments;
      parseElements(arg);
    }
  }

  private void parseElements(String arg) throws ArgsException {
    for (int i = 1; i < arg.length(); i++)
      parseElement(arg.charAt(i));
  }

  private void parseElement(char argChar) throws ArgsException {
    if (setArgument(argChar))
      argsFound.add(argChar);
    else {
      unexpectedArguments.add(argChar);
      errorCode = ErrorCode.UNEXPECTED_ARGUMENT;
      valid = false;
    }
  }

  private boolean setArgument(char argChar) throws ArgsException {
    ArgumentMarshaller m = marshallers.get(argChar);
    if (m == null) {
      return false;
    }
    try {
      m.set(currentArgument);
    } catch (ArgsException e) {
      valid = false;
      errorArgumentId = argChar;
      throw e;
    }

    return true;
  }

  public int cardinality() {
    return argsFound.size();
  }

  public String usage() {
    if (schema.length() > 0)
      return "-[" + schema + "]";
    else
      return "";
  }

  public String errorMessage() throws Exception {
    switch (errorCode) {
      case OK:
        throw new Exception("TILT: Should not get here.");
      case UNEXPECTED_ARGUMENT:
        return unexpectedArgumentMessage();
      case MISSING_STRING:
        return String.format("Could not find string parameter for -%c.",
            errorArgumentId);
      case INVALID_INTEGER:
        return String.format("Argument -%c expects an integer but was '%s'.",
            errorArgumentId, errorParameter);
      case MISSING_INTEGER:
        return String.format("Could not find integer parameter for -%c.",
            errorArgumentId);
      case MISSING_BOOLEAN:
        return String.format("Could not find boolean parameter for -%c.",
            errorArgumentId);
    }
    return "";
  }

  private String unexpectedArgumentMessage() {
    StringBuffer message = new StringBuffer("Argument(s) -");
    for (char c : unexpectedArguments) {
      message.append(c);
    }
    message.append(" unexpected.");

    return message.toString();
  }

  public String getString(char arg) {
    ArgumentMarshaller am = marshallers.get(arg);
    try {
      return (am == null) ? "" : (String) am.get();
    } catch (ClassCastException e) {
      return "";
    }
  }

  public int getInt(char arg) {
    ArgumentMarshaller am = marshallers.get(arg);
    try {
      return (am == null) ? 0 : (Integer) am.get();
    } catch (ClassCastException e) {
      return 0;
    }
  }

  public boolean getBoolean(char arg) {
    ArgumentMarshaller am = marshallers.get(arg);
    try {
      return (am != null) && (Boolean) am.get();
    } catch (ClassCastException e) {
      return false;
    }
  }

  public boolean has(char arg) {
    return argsFound.contains(arg);
  }

  public boolean isValid() {
    return valid;
  }

  private class ArgsException extends Exception {
  }

  private interface ArgumentMarshaller {
    void set(Iterator<String> currentArgument) throws ArgsException;
    Object get();
  }

  private class BooleanArgumentMarshaller implements ArgumentMarshaller {
    private boolean booleanValue = false;

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
      try {
        booleanValue = new Boolean(currentArgument.next());
      } catch (NoSuchElementException e) {
        errorCode = ErrorCode.MISSING_BOOLEAN;
        throw new ArgsException();
      }
    }

    @Override
    public Object get() {
      return booleanValue;
    }
  }

  private class StringArgumentMarshaller implements ArgumentMarshaller {
    private String stringValue;

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
      try {
        stringValue = currentArgument.next();
      } catch (NoSuchElementException e) {
        errorCode = ErrorCode.MISSING_STRING;
        throw new ArgsException();
      }
    }

    @Override
    public Object get() {
      return (stringValue == null) ? "" : stringValue;
    }
  }

  private class IntegerArgumentMarshaller implements ArgumentMarshaller {
    private Integer intValue;

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
      String parameter = null;
      try {
        parameter = currentArgument.next();
        intValue = new Integer(parameter);
      } catch (NoSuchElementException e) {
        errorCode = ErrorCode.MISSING_INTEGER;
        throw new ArgsException();
      } catch (NumberFormatException e) {
        errorParameter = parameter;
        errorCode = ErrorCode.INVALID_INTEGER;
        throw new ArgsException();
      }
    }

    @Override
    public Object get() {
      return (intValue == null) ? 0 : intValue;
    }
  }
}