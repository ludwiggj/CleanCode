package clean.code.chapter14.refactored.step.by.step;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ArgsTestFromBook {

  @Test
  public void testCreateWithNoSchemaOrArguments() throws Exception {
    Args args = new Args("", new String[0]);
    assertEquals(0, args.cardinality());
  }

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void testWithNoSchemaButWithOneArgument() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("", new String[]{"-x"});
    } catch (ArgsException e) {
      assertEquals(ArgsException.ErrorCode.UNEXPECTED_ARGUMENT,
          e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      throw e;
    }
  }

  @Test
   public void testWithSchemaButWithNoArguments() throws Exception {
     exception.expect(ArgsException.class);

     try {
       new Args("x", new String[0]);
     } catch (ArgsException e) {
       throw e;
     }
   }

  @Test
  public void testWithNoSchemaButWithMultipleArguments() throws Exception {
    try {
      exception.expect(ArgsException.class);

      new Args("", new String[]{"-x", "-y"});
    } catch (ArgsException e) {
      assertEquals(ArgsException.ErrorCode.UNEXPECTED_ARGUMENT,
          e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      throw e;
    }
  }

  @Test
  public void testNonLetterSchema() throws Exception {
    exception.expect(ArgsException.class);
    try {
      new Args("*", new String[]{});
    } catch (ArgsException e) {
      assertEquals(ArgsException.ErrorCode.INVALID_ARGUMENT_NAME,
          e.getErrorCode());
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
      assertEquals(ArgsException.ErrorCode.INVALID_ARGUMENT_FORMAT, e.getErrorCode());
      assertEquals('f', e.getErrorArgumentId());
      throw e;
    }
  }

  @Test
  public void testSimpleBooleanTruePresent() throws Exception {
    Args args = new Args("x", new String[]{"-x", "true"});
    assertEquals(1, args.cardinality());
    assertEquals(true, args.getBoolean('x'));
  }

  @Test
  public void testSimpleBooleanFalsePresent() throws Exception {
    Args args = new Args("x", new String[]{"-x", "false"});
    assertEquals(1, args.cardinality());
    assertEquals(false, args.getBoolean('x'));
  }

  @Test
  public void testMissingBooleanArgument() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x", new String[]{"-x"});
    } catch (ArgsException e) {
      assertEquals(ArgsException.ErrorCode.MISSING_BOOLEAN, e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      throw e;
    }
  }

  @Test
  public void testInvalidBoolean() throws Exception {
    Args args = new Args("x", new String[]{"-x", "Truthy"});
    assertEquals(1, args.cardinality());
    assertEquals(false, args.getBoolean('x'));
  }

  @Test
  public void testSpacesInFormat() throws Exception {
    Args args = new Args("x, y", new String[]{"-xy", "true", "false"});
    assertEquals(2, args.cardinality());
    assertTrue(args.has('x'));
    assertTrue(args.has('y'));
    assertEquals(true, args.getBoolean('x'));
    assertEquals(false, args.getBoolean('y'));
  }


  @Test
  public void testInvalidArgumentValueFormat() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x, y", new String[]{"xy", "true", "false"});
    } catch (ArgsException e) {
      assertEquals(ArgsException.ErrorCode.INVALID_ARGUMENT_FORMAT, e.getErrorCode());
      assertEquals('-', e.getErrorArgumentId());
      throw e;
    }
  }

  @Test
  public void testSimpleStringPresent() throws Exception {
    Args args = new Args("x*", new String[]{"-x", "param"});
    assertEquals(1, args.cardinality());
    assertTrue(args.has('x'));
    assertEquals("param", args.getString('x'));
  }

  @Test
  public void testMissingStringArgument() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x*", new String[]{"-x"});
    } catch (ArgsException e) {
      assertEquals(ArgsException.ErrorCode.MISSING_STRING, e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      throw e;
    }
  }

  @Test
  public void testSimpleIntPresent() throws Exception {
    Args args = new Args("x#", new String[]{"-x", "42"});
    assertEquals(1, args.cardinality());
    assertTrue(args.has('x'));
    assertEquals(42, args.getInt('x'));
  }

  @Test
  public void testInvalidInteger() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x#", new String[]{"-x", "Forty two"});
    } catch (ArgsException e) {
      assertEquals(ArgsException.ErrorCode.INVALID_INTEGER, e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      assertEquals("Forty two", e.getErrorParameter());
      throw e;
    }
  }

  @Test
  public void testMissingInteger() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x#", new String[]{"-x"});
    } catch (ArgsException e) {
      assertEquals(ArgsException.ErrorCode.MISSING_INTEGER, e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      throw e;
    }
  }

  @Test
  public void testSimpleDoublePresent() throws Exception {
    Args args = new Args("x##", new String[]{"-x", "42.3"});
    assertEquals(1, args.cardinality());
    assertTrue(args.has('x'));
    assertEquals(42.3, args.getDouble('x'), .001);
  }

  @Test
  public void testInvalidDouble() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x##", new String[]{"-x", "Forty two"});
    } catch (ArgsException e) {
      assertEquals(ArgsException.ErrorCode.INVALID_DOUBLE, e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      assertEquals("Forty two", e.getErrorParameter());
      throw e;
    }
  }

  @Test
  public void testMissingDouble() throws Exception {
    exception.expect(ArgsException.class);

    try {
      new Args("x##", new String[]{"-x"});
    } catch (ArgsException e) {
      assertEquals(ArgsException.ErrorCode.MISSING_DOUBLE, e.getErrorCode());
      assertEquals('x', e.getErrorArgumentId());
      throw e;
    }
  }
}