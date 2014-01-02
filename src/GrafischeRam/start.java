package GrafischeRam;

public class start{
	
	public static void main(String[] args){
		//String program = "HALT" + System.getProperty("line.separator");
		//String registers = "0";
		//Ram ram = new Ram(program, registers);
		Ram ram = new Ram();
		@SuppressWarnings("unused")
		Graphic GUI = new Graphic(ram);
	}
}
