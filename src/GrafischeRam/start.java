package GrafischeRam;

public class start{

	/**
	 * @param args
	 */
	public static void main(String[] args){
		String program = "R0 := 1 + 2" + System.getProperty("line.separator") + "HALT" + System.getProperty("line.separator");
		String registers = "0";
		Ram ram = new Ram(program, registers);
		@SuppressWarnings("unused")
		Graphic GUI = new Graphic(ram);
	}
}