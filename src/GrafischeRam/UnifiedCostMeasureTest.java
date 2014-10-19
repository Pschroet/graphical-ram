package GrafischeRam;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnifiedCostMeasureTest{
	static int numberTests = 0;
	Ram ram;
	
	@Before
	public void setUp(){
		this.ram = new Ram();
	}
	
	@After
	public void tearDown(){
		this.ram = null;
		numberTests++;
	}
	
	//tests for unified cost measure
	@Test
	public void test0(){
		String Program = "R0 := 1" + System.getProperty("line.separator") + "HALT";
		this.ram.load(Program, "0");
		System.out.println("Test " + numberTests + ":");
		System.out.println("Program:\n" + Program);
		int[] expectedResults = {1, 1};
		int[] expectedTotalResults = {1, 1};
		this.ram.computeLine();
		System.out.println("expected: " + expectedResults[0] + ", " + expectedResults[1] + " - result: " + this.ram.getCurrentUnifiedCost()[0] + ", " + this.ram.getCurrentUnifiedCost()[1]);
		assertArrayEquals(expectedResults, this.ram.getCurrentUnifiedCost());
		assertArrayEquals(expectedTotalResults, this.ram.getTotalUnifiedCost());
		this.ram.computeLine();
		expectedResults[0] = 0;
		expectedResults[1] = 0;
		System.out.println("expected: " + expectedResults[0] + ", " + expectedResults[1] + " - result: " + this.ram.getCurrentUnifiedCost()[0] + ", " + this.ram.getCurrentUnifiedCost()[1]);
		assertArrayEquals(expectedResults, this.ram.getCurrentUnifiedCost());
		assertArrayEquals(expectedTotalResults, this.ram.getTotalUnifiedCost());
	}
}
