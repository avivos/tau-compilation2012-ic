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
}
