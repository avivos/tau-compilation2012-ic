package IC;

import static org.junit.Assert.*;

import org.junit.Test;

public class IllegalCompilerTest {
	String testDir = "src/IC.test2/illegal_input/";
	// ILLEGAL FILES - TESTS
	@Test
	public void class_test_illegal() {
		assertTrue(TestUtils.runTestFile(testDir + "class_test_illegal.ic.bad", "-debug"));
	}
	@Test
	public void class_test2_illegal() {
		assertTrue(TestUtils.runTestFile(testDir + "ricki_test_error_lines.ic.bad", "-debug"));
	}
	
	@Test
	public void full_ast_test() {
		assertTrue(TestUtils.runTestFile(testDir + "full_ast_test.ic.bad", "-print-ast -debug"));
	}
	
	@Test
	public void BadAccess_test() {
		assertTrue(TestUtils.runTestFile(testDir + "BadAccess.ic.bad", "-print-ast -debug"));
	}
	
	@Test
	public void classNotDefine_test() {
		assertTrue(TestUtils.runTestFile(testDir + "classNotDefine.ic.bad", "-print-ast -debug"));
	}
	
	@Test
	public void DoubleMain_test() {
		assertTrue(TestUtils.runTestFile(testDir + "DoubleMain.ic.bad", "-print-ast -debug"));
	}
	
	@Test
	public void duplicatedField_test() {
		assertTrue(TestUtils.runTestFile(testDir + "duplicatedField.ic.bad", "-print-ast -debug"));
	}
	
	@Test
	public void MethodCallForNonClass_test() {
		assertTrue(TestUtils.runTestFile(testDir + "MethodCallForNonClass.ic.bad", "-print-ast -debug"));
	}
	
	@Test
	public void NonIntIndex_test() {
		assertTrue(TestUtils.runTestFile(testDir + "NonIntIndex.ic.bad", "-print-ast -debug"));
	}
	
	@Test
	public void NoSuchFunc_test() {
		assertTrue(TestUtils.runTestFile(testDir + "NoSuchFunc.ic.bad", "-print-ast -debug"));
	}
	
	@Test
	public void NoSuchStaticClass_test() {
		assertTrue(TestUtils.runTestFile(testDir + "NoSuchStaticClass.ic.bad", "-print-ast -debug"));
	}
	
	@Test
	public void NoSuchStaticFunc_test() {
		assertTrue(TestUtils.runTestFile(testDir + "NoSuchStaticFunc.ic.bad", "-print-ast -debug"));
	}
	
	@Test
	public void Overload_test() {
		assertTrue(TestUtils.runTestFile(testDir + "Overload.ic.bad", "-print-ast -debug"));
	}
	
	@Test
	public void Overload2_test() {
		assertTrue(TestUtils.runTestFile(testDir + "Overload2.ic.bad", "-print-ast -debug"));
	}
	
	@Test
	public void wrongArguments_test() {
		assertTrue(TestUtils.runTestFile(testDir + "wrongArguments.ic.bad", "-print-ast -debug"));
	}
}


