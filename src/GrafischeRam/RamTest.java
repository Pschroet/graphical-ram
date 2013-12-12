package GrafischeRam;

import org.junit.Test;

public class RamTest{
	int numberTests = 1;
	
	@Test
	public void test1(){
		System.out.println(numberTests++ + ". test");
		String program = "R0 := 1 + 2" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && ! run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test2(){
		System.out.println(numberTests++ + ". test");
		String program = "R0 :=  1 - 2" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test3(){
		System.out.println(numberTests++ + ". test");
		String program = "R0 :=  1 * 2" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test4(){
		System.out.println(numberTests++ + ". test");
		String program = "R0 :=  2 / 2" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test5(){
		System.out.println(numberTests++ + ". test");
		String program = "R0 := 1" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test6(){
		System.out.println(numberTests++ + ". test");
		String program = "L1 R0 := 1" + System.getProperty("line.separator") + "GZ R0, L1" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test7(){
		System.out.println(numberTests++ + ". test");
		String program = "L1 R0 := 0" + System.getProperty("line.separator") + "GZ R0, L1" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		int counter6 = 0;
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(counter6 + ": " + run);
			run = ram.computeLine();
			counter6++;
			if(counter6 == 4) break;
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test8(){
		System.out.println(numberTests++ + ". test");
		String program = "L1 R0 := 1" + System.getProperty("line.separator") + "GLZ R0, L1" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test9(){
		System.out.println(numberTests++ + ". test");
		String program = "L1 R0 := 1" + System.getProperty("line.separator") + "GLZ R0, L1" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		int counter8 = 0;
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(counter8 + ": " + run);
			run = ram.computeLine();
			counter8++;
			if(counter8 == 4) break;
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test10(){
		System.out.println(numberTests++ + ". test");
		String program = "L1 R0 := 0" + System.getProperty("line.separator") + "GGZ R0, L1" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test11(){
		System.out.println(numberTests++ + ". test");
		String program = "L1 R0 := 1" + System.getProperty("line.separator") + "GGZ R0, L1" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		int counter10 = 0;
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(counter10 + ": " + run);
			run = ram.computeLine();
			counter10++;
			if(counter10 == 4) break;
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test12(){
		System.out.println(numberTests++ + ". test");
		String program = "0 := 1" + System.getProperty("line.separator") + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test13(){
		System.out.println(numberTests++ + ". test");
		String program = "L1 R0 := 0" + System.getProperty("line.separator") + "GOTO L2" + System.getProperty("line.separator") + "GZ R0, L1" + System.getProperty("line.separator") + "L2 HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test14(){
		System.out.println(numberTests++ + ". test");
		String program = "R0 :=  -1 + -2" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test15(){
		System.out.println(numberTests++ + ". test");
		String program = "R0 :=  -16 / -2" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test16(){
		System.out.println(numberTests++ + ". test");
		String program = "R0 :=  -6 * 2" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test17(){
		System.out.println(numberTests++ + ". test");
		String program = "R0 :=  -1 - -5" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test18(){
		System.out.println(numberTests++ + ". test");
		String program = "R0 := 5" + System.getProperty("line.separator") + "L1 R0 := R0 - 1" + System.getProperty("line.separator") + "GZ R0, L2" + System.getProperty("line.separator") + "GOTO L1" + System.getProperty("line.separator") + "L2 HALT";
		String register = "0";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test19(){
		System.out.println(numberTests++ + ". test");
		String program = "L1 R1 := (R0) - 1" + System.getProperty("line.separator") + "GZ (R0), L2" + System.getProperty("line.separator") + "GOTO L1" + System.getProperty("line.separator") + "L2 HALT";
		String register = "1;5";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test20(){
		System.out.println(numberTests++ + ". test");
		String program = "R0 := 1" + System.getProperty("line.separator") + "R1 := (R0) - 1" + System.getProperty("line.separator") + "(R0) := R1 + 2" + System.getProperty("line.separator") + "L2 HALT";
		String register = "1;5;2";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}

	@Test
	public void test21(){
		System.out.println(numberTests++ + ". test");
		String program = "R0 := 1" + System.getProperty("line.separator") + "R1 := -R0 - 1" + System.getProperty("line.separator") + "L2 HALT";
		String register = "1;5;2";
		Ram ram = new Ram(program, register);
		String run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");
	}
	
	@Test
	public void test22(){
		/*System.out.println(numberTests++ + ". test");
		ram.load("C:\\Users\\Phil\\workspace\\Höhere Algorithmik\\loadTest.txt");
		run = ram.computeLine();
		while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && !run.matches("Syntax not allowed.*")){
			System.out.println(run);
			run = ram.computeLine();
		}
		System.out.println(run + "\n---------------------------------------------------------------------");*/
	}
}
