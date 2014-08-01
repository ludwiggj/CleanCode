package clean.code.chapter14.refactored.step.by.step;

import java.util.*;

import static clean.code.chapter14.refactored.step.by.step.ArgsException.ErrorCode.*;

public class Args {
  private String schema;
  private Map<Character, ArgumentMarshaller> marshallers =
      new HashMap<Character, ArgumentMarshaller>();
  private Set<Character> argsFound = new HashSet<Character>();

  private int noOfArguments = 0;
  private List<String> argsList;
  private Iterator<String> currentArgument;

  public Args(String schema, String[] args) throws ArgsException {
    this.schema = schema;
    this.argsList = Arrays.asList(args);
    parse();
  }

  private void parse() throws ArgsException {
    parseSchema();
    parseArguments();
  }

  private void parseSchema() throws ArgsException {
    for (String element : schema.split(",")) {
      if (element.length() > 0) {
        String trimmedElement = element.trim();
        parseSchemaElement(trimmedElement);
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
      throw new ArgsException(INVALID_ARGUMENT_FORMAT, elementId);
    }
  }

  private void validateSchemaElementId(char elementId) throws ArgsException {
    if (!Character.isLetter(elementId)) {
      throw new ArgsException(INVALID_ARGUMENT_NAME, elementId);
    }
  }

  private void parseArguments() throws ArgsException {
    for (currentArgument = argsList.iterator(); currentArgument.hasNext(); ) {
      String arg = currentArgument.next();
      parseArgument(arg);
    }
    if ((marshallers.size() > 0) && (noOfArguments == 0)) {
      throw new ArgsException(INVALID_ARGUMENT_FORMAT, '-');
    }
  }

  private void parseArgument(String arg) throws ArgsException {
    if (arg.startsWith("-")) {
      noOfArguments = ++noOfArguments;
      parseElements(arg);
    }
  }

  private void parseElements(String arg) throws ArgsException {
    for (int i = 1; i < arg.length(); i++) {
      parseElement(arg.charAt(i));
    }
  }

  private void parseElement(final char argChar) throws ArgsException {
    if (setArgument(argChar))
      argsFound.add(argChar);
    else {
      throw new ArgsException(UNEXPECTED_ARGUMENT, argChar);
    }
  }

  private boolean setArgument(char argChar) throws ArgsException {
    ArgumentMarshaller m = marshallers.get(argChar);
    if (m == null) {
      return false;
    }
    m.set(currentArgument, argChar);

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

  public double getDouble(char arg) {
    ArgumentMarshaller am = marshallers.get(arg);
    try {
      return (am == null) ? 0.0 : (Double) am.get();
    } catch (ClassCastException e) {
      return 0.0;
    }
  }

  public boolean has(char arg) {
    return argsFound.contains(arg);
  }
}