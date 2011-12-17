package IC;

import static org.junit.Assert.*;

import org.junit.Test;

public class LegalCompilerTest {

	String testDir = "src/IC.test2/legal_input/";
	// LEGAL FILES - TESTS
	@Test
	public void empty_program_file() {
		assertTrue(TestUtils.runTestFile(testDir + "program_test.ic.good", "-debug"));
	}
	
	@Test
	public void class_level() {
		assertTrue(TestUtils.runTestFile(testDir + "classDecl_test.ic.good", "-debug"));
	}
	
	@Test
	public void method_level() {
		assertTrue(TestUtils.runTestFile(testDir + "method_test.ic.good", "-debug"));
	}
	
	@Test
	public void type_level() {
		assertTrue(TestUtils.runTestFile(testDir + "field_test.ic.good", "-print-ast -debug"));
	}
	
	@Test
	public void method_with_statement() {
		assertTrue(TestUtils.runTestFile(testDir + "method_with_statement.ic.good", "-debug"));
	}
	
	@Test
	public void Quicksort() {
		assertTrue(TestUtils.runTestFile(testDir + "Quicksort.ic.good", "-debug"));
	}

	@Test
	public void Quicksort_modified() {
		assertTrue(TestUtils.runTestFile(testDir + "Quicksort_modified.ic.good", "-debug"));
	}
	
	@Test
	public void full_ast_test_legal() {
		assertTrue(TestUtils.runTestFile(testDir + "full_ast_test_legal.ic.good", "-debug"));
	}

	@Test
	public void full_ast_test() {
		assertTrue(TestUtils.runTestFile(testDir + "full_ast_test.ic.good", "-print-ast -debug"));
	}
	
}
