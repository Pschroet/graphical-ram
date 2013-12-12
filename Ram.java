package GrafischeRam;

import java.util.Hashtable;

public class Ram{
	
	String newLine = System.getProperty("line.separator");
	String program;
	String[] lines;
	Hashtable<String, Integer> labels;						//name of the labels (e.g. L1) and the corresponding line
	int currentLine;
	int[] Registers;
	
	public Ram(){
		this.Registers = new int[0];
		this.program = "";
		this.lines = new String[0];
		this.currentLine = -1;
	}
	//initiates all things necessary to compute the given program
	//p - program
	//r - registers
	public Ram(String p, String r){
		this.program = p;
		this.lines = partProgrammLines(this.program);
		labels = new Hashtable<String, Integer>();
		this.currentLine = 0;
		this.getLabels();
		this.fillRegisters(r);
	}
	
	//reads the contents of the given registers and transforms them into integer
	private void fillRegisters(String registers){
		//read the contents of the registers...
		String[] temp = registers.split(";");
		this.Registers = new int[temp.length];
		//and make each one an integer
		for(int i = 0; i < temp.length; i++){
			this.Registers[i] = Integer.parseInt(temp[i]);
		}
	}
	
	//splits the lines of the given program to make each line easy accessible and eliminates the newlines
	private String[] partProgrammLines(String p){
		return p.split(newLine);
	}
	
	//search for labels and save which line has which label
	//space at the beginning are not allowed!
	private void getLabels(){
		for(int i = 0; i < this.lines.length; i++){
			//if the current line has a label get the label and save it's position and compute the line
			if(this.lines[i].matches("^[L]\\d.*")){
				String[] restLine = this.lines[i].split("[L]\\d");
				String label = this.lines[i].split(" ")[0];
				this.labels.put(label, i);
				//put the rest back as the new line
				this.lines[i] = restLine[1].trim();
			}
		}
	}
	
	//set a new program and registers
	private void setProgramAndRegisters(String p, String r){
		this.program = p;
		this.lines = partProgrammLines(this.program);
		labels = new Hashtable<String, Integer>();
		this.currentLine = 0;
		this.getLabels();
		this.fillRegisters(r);
	}
	
	//compute one line of the ram and give the output
	String computeLine(){
		String computingLine = this.lines[this.currentLine].replace(" ", "");
		//if a HALT comes, end the program
		if(computingLine.matches("HALT")){
			return "End of program reached";
		}
		if(computingLine.matches("R[0-9]+:=((R[0-9]+)|([(]R[0-9]+[)])|(\\-[0-9]+)|[0-9]+)((\\+|\\-|\\*|\\/)((R[0-9]+)|([(]R[0-9]+[)])|(\\-[0-9]+)|([0-9]+)))?")){
			String[] currentContent = computingLine.split(":=");
			//check the left part
			int leftElement;
			if(currentContent[0].trim().matches("[0-9]*")){
				return "Syntax not allowed: Left element of an assignment may not be a constant.";
			}
			if(currentContent[0].trim().matches("[(].*[)]")){
				//element is indirect, so remove parentheses and 'R'
				int indirectRegister = Integer.parseInt(currentContent[0].replace("(", "").replace(")", "").replace("R", ""));
				leftElement = this.Registers[indirectRegister];
			}
			else{
				leftElement = Integer.parseInt(currentContent[0].replace("R", ""));
			}
			//check the right part
			String rightElement = currentContent[1];
			if(rightElement.matches("[0-9]*")){
				//go to next line
				this.currentLine++;
				this.Registers[leftElement] = Integer.parseInt(rightElement);
				return "Set Register " + leftElement + " to " + Integer.parseInt(rightElement);
			}
			if(currentContent[1].matches(".*\\+.*")){
				String[] rightHalfs = currentContent[1].split("\\+");
				//check what the left part of the right element is
				int rightLeftHalf = getElement(rightHalfs[0]);
				//check what the right part of the right element is
				int rightRightHalf = getElement(rightHalfs[1]);
				//go to next line
				this.currentLine++;
				this.Registers[leftElement] = rightLeftHalf + rightRightHalf;
				return "Set Register " + leftElement + " to " + (rightLeftHalf + rightRightHalf);
			}
			if(currentContent[1].matches(".*\\*.*")){
				String[] rightHalfs = currentContent[1].split("\\*");
				rightHalfs[0] = rightHalfs[0];
				rightHalfs[1] = rightHalfs[1];
				//check what the left part of the right element is
				int rightLeftHalf = getElement(rightHalfs[0]);
				//check what the right part of the right element is
				int rightRightHalf = getElement(rightHalfs[1]);
				//go to next line
				this.currentLine++;
				this.Registers[leftElement] = rightLeftHalf * rightRightHalf;
				return "Set Register " + leftElement + " to " + (rightLeftHalf * rightRightHalf);
				
			}
			if(currentContent[1].matches(".*/.*")){
				String[] rightHalfs = currentContent[1].split("/");
				rightHalfs[0] = rightHalfs[0];
				rightHalfs[1] = rightHalfs[1];
				//check what the left part of the right element is
				int rightLeftHalf = getElement(rightHalfs[0]);
				//check what the right part of the right element is
				int rightRightHalf = getElement(rightHalfs[1]);
				//go to next line
				this.currentLine++;
				this.Registers[leftElement] = rightLeftHalf / rightRightHalf;
				return "Set Register " + leftElement + " to " + (rightLeftHalf / rightRightHalf);
			}
			if(currentContent[1].matches(".*\\-.*")){
				//splits the assignment using the minuses, i. e. that if there are negative values their minuses will be erased; this case is handled, see below
				String[] rightHalfs = currentContent[1].split("\\-");
				//check what the left and right parts of the right element are
				int rightLeftHalf;
				int rightRightHalf;
				//if the first element is a white space and nothing else then there was a minus before the number
				if(rightHalfs[0].matches("")){
					//that's why the next element will be used
					rightLeftHalf = getElement("-" + rightHalfs[1]);
					if(rightHalfs[2].matches("")){
						rightRightHalf = getElement("-" + rightHalfs[3]);
					}
					else{
						rightRightHalf = getElement(rightHalfs[2]);
					}
				}
				//if the first element is no white space then the number will be used,
				else{
					rightLeftHalf = getElement(rightHalfs[0]);
					//but the second could be negative, so it is checked
					if(rightHalfs[1].matches("")){
						rightRightHalf = getElement("-" + rightHalfs[2]);
					}
					else{
						rightRightHalf = getElement(rightHalfs[1]);
					}
				}
				//go to next line
				this.currentLine++;
				this.Registers[leftElement] = rightLeftHalf - rightRightHalf;
				return "Set Register " + leftElement + " to " + (rightLeftHalf - rightRightHalf);
			}
		}
		if(computingLine.matches("GGZ(R[0-9]*|[(]R[0-9]*[)]),L[0-9]*")){
			String[] elements = computingLine.replace("GGZ", "").replace("R", "").split(",");
			if(elements[0].matches("[(][0-9]*[)]")){
				int indirectRegister = this.Registers[this.Registers[Integer.parseInt(elements[0].replace("(", "").replace(")", ""))]];
				if(indirectRegister > 0){
					this.currentLine = this.labels.get(elements[1]);
					return "Register " + indirectRegister + " == 0" + ". Go to line " + this.currentLine;
				}
				else{
					this.currentLine++;
					return "Register " + indirectRegister + " != 0" + ". Just go one line further";
				}
			}
			else{
				if(this.Registers[Integer.parseInt(elements[0])] > 0){
					this.currentLine = this.labels.get(elements[1]);
					return "Register " + elements[0] + " == 0" + ". Go to line " + this.currentLine;
				}
				else{
					this.currentLine++;
					return "Register " + elements[0] + " != 0" + ". Just go one line further";
				}
			}
		}
		if(computingLine.matches("GLZ(R[0-9]*|[(]R[0-9]*[)]),L[0-9]*")){
			String[] elements = computingLine.replace("GLZ", "").replace("R", "").split(",");
			if(elements[0].matches("[(][0-9]*[)]")){
				int indirectRegister = this.Registers[this.Registers[Integer.parseInt(elements[0].replace("(", "").replace(")", ""))]];
				if(indirectRegister < 0){
					this.currentLine = this.labels.get(elements[1]);
					return "Register " + indirectRegister + " == 0" + ". Go to line " + this.currentLine;
				}
				else{
					this.currentLine++;
					return "Register " + indirectRegister + " != 0" + ". Just go one line further";
				}
			}
			else{
				if(this.Registers[Integer.parseInt(elements[0])] < 0){
					this.currentLine = this.labels.get(elements[1]);
					return "Register " + elements[0] + " == 0" + ". Go to line " + this.currentLine;
				}
				else{
					this.currentLine++;
					return "Register " + elements[0] + " != 0" + ". Just go one line further";
				}
			}
		}
		if(computingLine.matches("GZ(R[0-9]*|[(]R[0-9]*[)]),L[0-9]*")){
			String[] elements = computingLine.replace("GZ", "").replace("R", "").split(",");
			if(elements[0].matches("[(][0-9]*[)]")){
				int indirectRegister = this.Registers[this.Registers[Integer.parseInt(elements[0].replace("(", "").replace(")", ""))]];
				if(indirectRegister == 0){
					this.currentLine = this.labels.get(elements[1]);
					return "Register " + indirectRegister + " == 0" + ". Go to line " + this.currentLine;
				}
				else{
					this.currentLine++;
					return "Register " + indirectRegister + " != 0" + ". Just go one line further";
				}
			}
			else{
				if(this.Registers[Integer.parseInt(elements[0])] == 0){
					this.currentLine = this.labels.get(elements[1]);
					return "Register " + elements[0] + " == 0" + ". Go to line " + this.currentLine;
				}
				else{
					this.currentLine++;
					return "Register " + elements[0] + " != 0" + ". Just go one line further";
				}
			}
		}
		//if there is a GOTO just set the currentLine to the one which has the label
		if(computingLine.matches("GOTO[L][0-9]*")){
			String nextLabel = computingLine.replace("GOTO", "");
			int nextLine = this.labels.get(nextLabel);
			this.currentLine = nextLine;
			return "Going to label " + nextLabel + " in line " + (nextLine + 1);
		}
		return "No possiblity to compute further. Is the syntax correct?";
	}
	
	//checks a halve of an assignment and returns it's value
	int getElement(String half){
		if(half.matches("[(].*[)]")){
			//System.out.println("a link to a register");
			return this.Registers[this.Registers[Integer.parseInt(half.replace("(", "").replace(")", "").replace("R", ""))]];
		}
		//register
		if(half.matches("R[0-9]*")){
			//System.out.println("a register");
			return this.Registers[Integer.parseInt(half.replace("R", ""))];
		}
		//constant
		if(half.matches("[0-9]*")){
			//System.out.println("a number");
			return Integer.parseInt(half);
		}
		//negative constant
		if(half.matches("\\-[0-9]*")){
			//System.out.println("a number");
			return Integer.parseInt(half);
		}
		return 0;
	}
	
	//load a new set of registers and program and sets these to be the new registers and program to be computed
	void load(String newProgram, String newRegisters){
		this.setProgramAndRegisters(newProgram, newRegisters);
	}
}
