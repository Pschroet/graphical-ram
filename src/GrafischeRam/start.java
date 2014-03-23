package GrafischeRam;

public class start{
	
	public static void main(String[] args){
		Ram ram = new Ram();
		@SuppressWarnings("unused")
		GraphicSwing GUI = new GraphicSwing(ram);
	}
}
