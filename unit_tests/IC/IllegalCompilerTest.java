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
}


