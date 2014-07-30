package clean.code.chapter14.refactored.step.by.step;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;

import static org.junit.Assert.*;

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
    Args args = new Args("", new String[]{"-x"});
    assertEquals(false, args.isValid());
    assertEquals("Argument(s) -x unexpected.", args.errorMessage());
  }

  @Test
  public void shouldIdentifyUnexpectedArgumentsIfNoSchemaButMultipleArguments() throws Exception {
    Args args = new Args("", new String[]{"-x", "-y"});
    assertEquals(false, args.isValid());
    assertEquals("Argument(s) -xy unexpected.", args.errorMessage());
  }

  @Test
  public void shouldIdentifyNonLetterSchema() throws Exception {
    exception.expect(ParseException.class);
    exception.expectMessage("Bad character:*in Args format: *");

    new Args("*", new String[]{});
  }

  @Test
  public void shouldIdentifyInvalidArgumentFormat() throws Exception {
    exception.expect(ParseException.class);
    exception.expectMessage("Argument: f has invalid format: ~.");

    new Args("f~", new String[]{});
  }

  @Test
  public void shouldAllowSpacesInArgumentFormat() throws Exception {
    Args args = new Args("x, y", new String[]{"-xy", "true", "true"});
    assertEquals(2, args.cardinality());
    assertTrue(args.has('x'));
    assertTrue(args.has('y'));
    assertEquals(true, args.getBoolean('x'));
    assertEquals(true, args.getBoolean('y'));
  }

  @Test
  public void shouldIdentifyInvalidArgumentNameFormat() throws Exception {
    Args args = new Args("x,y", new String[]{"xy", "true", "false"});
    assertEquals(false, args.isValid());
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
  public void getBooleanShouldReturnFalseIfArgumentNameIsNotInSchemaAndOtherArgumentsAreIncorrect() throws Exception {
    Args args = new Args("x", new String[]{"-y", "true"});
    assertEquals(false, args.isValid());
    assertEquals("Argument(s) -y unexpected.", args.errorMessage());
    assertEquals(false, args.getBoolean('y'));
  }

  @Test
  public void getBooleanShouldReturnFalseIfArgumentNameIsNotInSchemaAndOtherArgumentsAreCorrect() throws Exception {
    Args args = new Args("x", new String[]{"-x", "true"});
    assertEquals(true, args.isValid());
    assertEquals(false, args.getBoolean('y'));
  }

  @Test
  public void getBooleanShouldReturnFalseIfArgumentNameIsNotSupplied() throws Exception {
    Args args = new Args("x", new String[]{});
    assertEquals(false, args.getBoolean('x'));
    assertEquals(false, args.isValid());
  }

  @Test
  public void getBooleanShouldReturnFalseIfArgumentValueIsNotSupplied() throws Exception {
    Args args = new Args("x", new String[]{"-x"});
    assertEquals(false, args.getBoolean('x'));
    assertEquals(false, args.isValid());
    assertEquals("Could not find boolean parameter for -x.", args.errorMessage());
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
  public void getStringShouldReturnEmptyStringIfArgumentNameIsNotInSchemaAndOtherArgumentsAreIncorrect() throws Exception {
    Args args = new Args("x*", new String[]{"-y", "23"});
    assertEquals(false, args.isValid());
    assertEquals("Argument(s) -y unexpected.", args.errorMessage());
    assertEquals("", args.getString('y'));
  }

  @Test
  public void getStringShouldReturnEmptyStringIfArgumentNameIsNotInSchemaAndOtherArgumentsAreCorrect() throws Exception {
    Args args = new Args("x*", new String[]{"-x", "23"});
    assertEquals(true, args.isValid());
    assertEquals("", args.getString('y'));
  }

  @Test
  public void getStringShouldReturnEmptyStringIfArgumentNameIsNotSupplied() throws Exception {
    Args args = new Args("x*", new String[]{});
    assertEquals("", args.getString('x'));
    assertEquals(false, args.isValid());
  }

  @Test
  public void getStringShouldReturnEmptyStringIfArgumentValueIsNotSupplied() throws Exception {
    Args args = new Args("x*", new String[]{"-x"});
    assertEquals(false, args.isValid());
    assertEquals("Could not find string parameter for -x.", args.errorMessage());
    assertEquals("", args.getString('x'));
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
    Args args = new Args("x#", new String[]{"-x", "Forty two"});
    assertEquals(false, args.isValid());
    assertEquals("Argument -x expects an integer but was 'Forty two'.",
        args.errorMessage());
  }

  @Test
  public void shouldSupportMultipleIntegers() throws Exception {
    Args args = new Args("x#,y#", new String[]{"-xy", "25", "49"});
    assertEquals(2, args.cardinality());
    assertEquals(25, args.getInt('x'));
    assertEquals(49, args.getInt('y'));
  }

  @Test
  public void getIntegerShouldReturnZeroIfArgumentNameIsNotInSchemaAndOtherArgumentsAreIncorrect() throws Exception {
    Args args = new Args("x#", new String[]{"-y", "23"});
    assertEquals(false, args.isValid());
    assertEquals("Argument(s) -y unexpected.", args.errorMessage());
    assertEquals(0, args.getInt('y'));
  }

  @Test
  public void getIntegerShouldReturnZeroIfArgumentNameIsNotInSchemaAndOtherArgumentsAreCorrect() throws Exception {
    Args args = new Args("x#", new String[]{"-x", "23"});
    assertEquals(true, args.isValid());
    assertEquals(0, args.getInt('y'));
  }

  @Test
  public void getIntegerShouldReturnZeroIfArgumentNameIsNotSupplied() throws Exception {
    Args args = new Args("x#", new String[]{});
    assertEquals(0, args.getInt('x'));
    assertEquals(false, args.isValid());
  }

  @Test
  public void getIntegerShouldReturnZeroIfArgumentValueIsNotSupplied() throws Exception {
    Args args = new Args("x#", new String[]{"-x"});
    assertEquals(false, args.isValid());
    assertEquals("Could not find integer parameter for -x.", args.errorMessage());
    assertEquals(0, args.getInt('x'));
  }

  // Double
  @Test
  public void shouldSupportDouble() throws Exception {
    Args args = new Args("x##", new String[] {"-x", "42.3"});
    assertTrue(args.isValid());
    assertEquals(1, args.cardinality());
    assertTrue(args.has('x'));
    assertEquals(42.3, args.getDouble('x'), .001);
  }

  @Test
  public void shouldReportErrorWhenDoubleArgumentValueIsWrongType() throws Exception {
    Args args = new Args("x##", new String[] {"-x", "Forty two"});
    assertFalse(args.isValid());
    assertEquals(0, args.cardinality());
    assertFalse(args.has('x'));
    assertEquals(0, args.getInt('x'));
    assertEquals("Argument -x expects a double but was 'Forty two'.", args.errorMessage());
  }

  @Test
  public void getDoubleShouldReturnZeroIfArgumentNameIsNotInSchemaAndOtherArgumentsAreCorrect() throws Exception {
    Args args = new Args("x##", new String[] {"-x", "25.4"});
    assertTrue(args.isValid());
    assertEquals(0.0, args.getDouble('y'), 0.01);
  }

  @Test
  public void getDoubleShouldReturnZeroIfArgumentNameIsNotSupplied() throws Exception {
    Args args = new Args("x##", new String[] {});
    assertFalse(args.isValid());
    assertEquals(0.0, args.getDouble('x'), 0.01);
  }

  @Test
  public void getDoubleShouldReturnZeroIfArgumentValueIsNotSupplied() throws Exception {
    Args args = new Args("x##", new String[] {"-x"});
    assertFalse(args.isValid());
    assertEquals(0, args.cardinality());
    assertFalse(args.has('x'));
    assertEquals(0.0, args.getDouble('x'), 0.01);
    assertEquals("Could not find double parameter for -x.",
            args.errorMessage());
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