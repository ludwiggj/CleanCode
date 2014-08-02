package clean.code.chapter14.refactored.step.by.step;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static clean.code.chapter14.refactored.step.by.step.ArgsException.ErrorCode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ArgsTest {

  @Test
  public void cardinalityShouldBeZeroIfNoSchemaOrArguments() throws Exception {
    Args args = new Args("", new String[0]);
    assertEquals(0, args.cardinality());
  }

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void shouldIdentifyUnexpectedArgumentIfNoSchemaButOneArgument() throws Exception {
    exception.expect(ArgsException.class);
    try {
      new Args("", new String[]{"-x"});
    } catch (ArgsException e) {
      assertEquals(UNEXPECTED_ARGUMENT, e.getErrorCode());
      assertEquals("Argument -x unexpected.", e.errorMessage());
      assertEquals('x', e.getErrorArgumentId());

      throw e;
    }
  }

  @Test
  public void shouldIdentifyFirstUnexpectedArgumentIfNoSchemaButMultipleArguments() throws Exception {
    exception.expect(ArgsException.class);
    try {
      new Args("", new String[]{"-x", "-y"});
    } catch (ArgsException e) {
      assertEquals(UNEXPECTED_ARGUMENT, e.getErrorCode());
      assertEquals("Argument -x unexpected.", e.errorMessage());
      assertEquals('x', e.getErrorArgumentId());

      throw e;
    }
  }

  @Test
  public void shouldIdentifyNonLetterSchema() throws Exception {
    exception.expect(ArgsException.class);
    try {
      new Args("*", new String[]{});
    } catch (ArgsException e) {
      assertEquals(INVALID_ARGUMENT_NAME, e.getErrorCode());
      assertEquals("Bad character:* in Args format", e.errorMessage());
      assertEquals('*', e.getErrorArgumentId());

      throw e;
    }
  }

  @Test
  public void testInvalidArgumentFormat() throws Exception {
    exception.expect(ArgsException.class);
    try {
      new Args("f~", new String[]{});
    } catch (ArgsException e) {
      assertEquals(INVALID_ARGUMENT_FORMAT, e.getErrorCode());
      assertEquals('f', e.getErrorArgumentId());
      throw e;
    }
  }

  @Test
  public void shouldAllowSpacesInArgumentFormat() throws Exception {
    Args args = new Args("x, y", new String[]{"-xy", "true", "false"});
    assertEquals(2, args.cardinality());
    assertTrue(args.has('x'));
    assertTrue(args.has('y'));
    assertEquals(true, args.getBoolean('x'));
    assertEquals(false, args.getBoolean('y'));
  }

  @Test
  public void shouldIdentifyInvalidArgumentNameFormat1() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x,y", new String[]{"xy", "true", "false"});
    } catch (ArgsException e) {
      assertEquals(INVALID_ARGUMENT_FORMAT, e.getErrorCode());
      assertEquals('-', e.getErrorArgumentId());
      throw e;
    }
  }

  @Test
  public void shouldIdentifyInvalidArgumentNameFormat2() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x*", new String[]{});
    } catch (ArgsException e) {
      assertEquals(INVALID_ARGUMENT_FORMAT, e.getErrorCode());
      assertEquals('-', e.getErrorArgumentId());
      throw e;
    }
  }

  // Boolean tests...
  @Test
  public void shouldSupportBooleanTrue() throws Exception {
    Args args = new Args("x", new String[]{"-x", "true"});
    assertEquals(1, args.cardinality());
    assertEquals(true, args.getBoolean('x'));
  }

  @Test
  public void shouldSupportBooleanFalse() throws Exception {
    Args args = new Args("x", new String[]{"-x", "false"});
    assertEquals(1, args.cardinality());
    assertEquals(false, args.getBoolean('x'));
  }

  @Test
  public void getBooleanShouldReturnFalseIfArgumentValueIsNeitherTrueNorFalse() throws Exception {
    Args args = new Args("x", new String[]{"-x", "Truthy"});
    assertEquals(1, args.cardinality());
    assertEquals(false, args.getBoolean('x'));
  }

  @Test
  public void shouldSupportMultipleBooleans() throws Exception {
    Args args = new Args("x,y", new String[]{"-xy", "true", "true"});
    assertEquals(2, args.cardinality());
    assertEquals(true, args.getBoolean('x'));
    assertEquals(true, args.getBoolean('y'));
  }

  @Test
  public void getBooleanShouldReturnFalseIfArgumentNameIsNotInSchemaAndOtherArgumentsAreCorrect() throws Exception {
    Args args = new Args("x", new String[]{"-x", "true"});
    assertEquals(false, args.getBoolean('y'));
  }

  @Test
  public void shouldIdentifyMissingBoolean() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x", new String[]{"-x"});
    } catch (ArgsException e) {
      assertEquals(MISSING_BOOLEAN, e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      assertEquals("Could not find boolean parameter for -x.", e.errorMessage());
      throw e;
    }
  }

  // String tests...
  @Test
  public void testSimpleStringPresent() throws Exception {
    Args args = new Args("x*", new String[]{"-x", "param"});
    assertEquals(1, args.cardinality());
    assertTrue(args.has('x'));
    assertEquals("param", args.getString('x'));
  }

  @Test
  public void shouldSupportMultipleStrings() throws Exception {
    Args args = new Args("x*,y*", new String[]{"-xy", "25", "49"});
    assertEquals(2, args.cardinality());
    assertEquals("25", args.getString('x'));
    assertEquals("49", args.getString('y'));
  }

  @Test
  public void getStringShouldReturnEmptyStringIfArgumentNameIsNotInSchemaAndOtherArgumentsAreCorrect() throws Exception {
    Args args = new Args("x*", new String[]{"-x", "23"});
    assertEquals("", args.getString('y'));
  }

  @Test
  public void shouldIdentifyMissingString() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x*", new String[]{"-x"});
    } catch (ArgsException e) {
      assertEquals(MISSING_STRING, e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      assertEquals("Could not find string parameter for -x.", e.errorMessage());
      throw e;
    }
  }

  // Integer tests...
  @Test
  public void shouldSupportInteger() throws Exception {
    Args args = new Args("x#", new String[]{"-x", "42"});
    assertEquals(1, args.cardinality());
    assertTrue(args.has('x'));
    assertEquals(42, args.getInt('x'));
  }

  @Test
  public void shouldReportErrorWhenIntegerArgumentValueIsWrongType() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x#", new String[]{"-x", "Forty two"});
    } catch (ArgsException e) {
      assertEquals(INVALID_INTEGER, e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      assertEquals("Argument -x expects an integer but was 'Forty two'.",
          e.errorMessage());
      throw e;
    }
  }

  @Test
  public void shouldSupportMultipleIntegers() throws Exception {
    Args args = new Args("x#,y#", new String[]{"-xy", "25", "49"});
    assertEquals(2, args.cardinality());
    assertEquals(25, args.getInt('x'));
    assertEquals(49, args.getInt('y'));
  }

  @Test
  public void getIntegerShouldReturnZeroIfArgumentNameIsNotInSchemaAndOtherArgumentsAreCorrect() throws Exception {
    Args args = new Args("x#", new String[]{"-x", "23"});
    assertEquals(0, args.getInt('y'));
  }

  @Test
  public void shouldIdentifyMissingInteger() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x#", new String[]{"-x"});
    } catch (ArgsException e) {
      assertEquals(MISSING_INTEGER, e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      assertEquals("Could not find integer parameter for -x.", e.errorMessage());
      throw e;
    }
  }

  // Double
  @Test
  public void shouldSupportDouble() throws Exception {
    Args args = new Args("x##", new String[]{"-x", "42.3"});
    assertEquals(1, args.cardinality());
    assertTrue(args.has('x'));
    assertEquals(42.3, args.getDouble('x'), .001);
  }

  @Test
  public void shouldReportErrorWhenDoubleArgumentValueIsWrongType() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x##", new String[]{"-x", "Forty two"});
    } catch (ArgsException e) {
      assertEquals(INVALID_DOUBLE, e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      assertEquals("Argument -x expects a double but was 'Forty two'.",
          e.errorMessage());
      throw e;
    }
  }

  @Test
  public void getDoubleShouldReturnZeroIfArgumentNameIsNotInSchemaAndOtherArgumentsAreCorrect() throws Exception {
    Args args = new Args("x##", new String[]{"-x", "25.4"});
    assertEquals(0.0, args.getDouble('y'), 0.01);
  }

  @Test
  public void shouldIdentifyMissingDouble() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x##", new String[]{"-x"});
    } catch (ArgsException e) {
      assertEquals(MISSING_DOUBLE, e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      assertEquals("Could not find double parameter for -x.", e.errorMessage());
      throw e;
    }
  }

  // Other tests...
  @Test
  public void shouldReturnFalseIfCallGetBooleanOnNonBooleanArgument() throws Exception {
    Args args = new Args("x#", new String[]{"-x", "42"});
    assertEquals(false, args.getBoolean('x'));
  }

  @Test
  public void shouldReturnEmptyStringIfCallGetStringOnNonStringArgument() throws Exception {
    Args args = new Args("x#", new String[]{"-x", "42"});
    assertEquals("", args.getString('x'));
  }

  @Test
  public void shouldReturnZeroIfCallGetIntOnNonIntegerArgument() throws Exception {
    Args args = new Args("x", new String[]{"-x", "false"});
    assertEquals(0, args.getInt('x'));
  }
}