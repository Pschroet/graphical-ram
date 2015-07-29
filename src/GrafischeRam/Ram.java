package GrafischeRam;

import java.util.Hashtable;

public class Ram{
	
	String program;
	String[] lines;
	Hashtable<String, Integer> labels;						//name of the labels (e.g. L1) and the corresponding line
	int currentLine;
	int[] Registers;
	private int ucmOrder;
	private int ucmMemory;
	private int ucmOrderTotal;
	private int ucmMemoryTotal;
	private double lcmOrder;
	private double lcmOrderTotal;
	private String syntaxErrors;
	
	public Ram(){
		this.Registers = new int[0];
		this.program = "";
		this.lines = new String[0];
		this.currentLine = 0;
		this.ucmOrder = 0;
		this.ucmOrderTotal = 0;
		this.ucmMemory = 0;
		this.ucmMemoryTotal = 0;
		this.lcmOrder = 0;
		this.lcmOrderTotal = 0;
		this.syntaxErrors = "";
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
		this.ucmOrder = 0;
		this.ucmOrderTotal = 0;
		this.ucmMemory = 0;
		this.ucmMemoryTotal = 0;
		this.lcmOrder = 0;
		this.lcmOrderTotal = 0;
		this.syntaxErrors = "";
	}
	
	//reads the contents of the given registers and transforms them into integer
	private void fillRegisters(String registers){
		if(!registers.equals("")){
			//read the contents of the registers...
			String[] temp = registers.split(";");
			this.Registers = new int[temp.length];
			//and make each one an integer
			for(int i = 0; i < temp.length; i++){
				this.Registers[i] = Integer.parseInt(temp[i].trim());
			}
		}
		else{
			this.Registers = new int[0];
		}
	}
	
	void addNewRegister(){
		int[] newRegister = new int[this.Registers.length + 1];
		for(int i = 0; i < this.Registers.length; i++){
			newRegister[i] = this.Registers[i];
		}
		newRegister[this.Registers.length] = 0;
		this.Registers = newRegister;
	}
	
	void addNewRegister(int content){
		int[] newRegister = new int[this.Registers.length + 1];
		for(int i = 0; i < this.Registers.length; i++){
			newRegister[i] = this.Registers[i];
		}
		newRegister[this.Registers.length] = content;
		this.Registers = newRegister;
	}
	
	//splits the lines of the given program to make each line easy accessible and eliminates the newlines
	private String[] partProgrammLines(String p){
		//String[] tmp = p.split(System.getProperty("line.separator"));
		//for(int i = 0; i < tmp.length; i++){
		//	System.out.println(i + ": " + tmp[i]);
		//}
		return p.split(System.getProperty("line.separator"));
	}
	
	//search for labels and save which line has which label
	//spaces at the beginning are not allowed!
	private void getLabels(){
		for(int i = 0; i < this.lines.length; i++){
			String label = this.lines[i].split(" ")[0];
			if(label.matches("[L]\\d+")){
				this.labels.put(label, i);
				String[] labelAndLine = this.lines[i].split("^[L]\\d+");
				if(labelAndLine.length > 1){
					this.lines[i] = labelAndLine[1].trim();
				}
				else{
					this.lines[i] = "";
				}
			}
		}
	}
	
	//set a new program and registers
	private void setProgramAndRegisters(String p, String r){
		this.program = p;
		this.lines = partProgrammLines(this.program);
		/*for(int i = 0; i < this.lines.length; i++){
			System.out.println(i + ": " + this.lines[i]);
		}*/
		this.labels = new Hashtable<String, Integer>();
		this.currentLine = 0;
		this.getLabels();
		this.fillRegisters(r);
		this.ucmOrder = 0;
		this.ucmOrderTotal = 0;
		this.ucmMemory = 0;
		this.ucmMemoryTotal = 0;
		this.lcmOrder = 0;
		this.lcmOrderTotal = 0;
	}
	
	//compute one line of the ram and give the output
	String computeLine(){
		if(this.currentLine >= this.lines.length){
			return "Error. End of program reached, but HALT is missing.";
		}
		this.ucmMemory = 0;
		this.ucmOrder = 0;
		this.lcmOrder = 0;
		//ignore all whitespace characters
		String computingLine = this.lines[this.currentLine].replaceAll("\\s", "");
		//System.out.println("computing: " + computingLine);
		//if a HALT comes, end the program
		if(computingLine.matches("HALT")){
			return "End of program reached.";
		}
		//if the current line is empty or contains only white spaces
		if(computingLine.matches("") || computingLine.matches("\\s*")){
			this.currentLine++;
			return "Going one line further.";
		}
		//if the current line is just a comment
		if(computingLine.matches("[#]+.*")){
			this.currentLine++;
			return "Comment. Going one line further.";
		}
		//if there is a comment later in the line, remove the comment from the parsed part
		if(computingLine.matches(".+[#]+.*")){
			computingLine = computingLine.split("[#]")[0];
		}
		//System.out.println("computingLine: " + computingLine);
		try{
			if(computingLine.matches("((R[0-9]+)|([(]R[0-9]+[)])):=((R[0-9]+)|([(]R[0-9]+[)])|(\\-[0-9]+)|[0-9]+)((\\+|\\-|\\*|\\/)((R[0-9]+)|([(]R[0-9]+[)])|(\\-[0-9]+)|([0-9]+)))?")){
				this.ucmOrder++;
				this.ucmOrderTotal++;
				String[] currentContent = computingLine.split(":=");
				//check the left part
				int leftElement = 0;
				if(currentContent[0].trim().matches("(\\-)?[0-9]+")){
					return "Syntax not allowed: Left element of an assignment may not be a constant.";
				}
				else{
					if(currentContent[0].matches("^R\\d+")){
						leftElement = Integer.parseInt(currentContent[0].replace("R", "").replace(" ", ""));
						this.ucmMemory++;
						this.ucmMemoryTotal++;
						this.lcmOrder += this.calculateLogarithmicCost(leftElement);
					}
					else{
						if(currentContent[0].matches("^[(]R\\d+[)]")){
							leftElement = this.Registers[Integer.parseInt(currentContent[0].replace("(", "").replace(")", "").replace("R", "").replace(" ", ""))];
							this.ucmMemory++;
							this.ucmMemoryTotal++;
							this.lcmOrder += this.calculateLogarithmicCost(leftElement);
							this.lcmOrder += this.calculateLogarithmicCost(this.Registers[leftElement]);
						}
						
					}
				}
				//check the right part
				String rightElement = currentContent[1];
				//if it is a constant
				if(rightElement.matches("(\\-)?[0-9]+")){
					int number = Integer.parseInt(rightElement);
					this.Registers[leftElement] = number;
					this.lcmOrder += this.calculateLogarithmicCost(number);
					this.lcmOrderTotal += this.lcmOrder;
					//go to next line
					this.currentLine++;
					return "Set Register " + leftElement + " to " + Integer.parseInt(rightElement) + ".";
				}
				//if the right element is just a register
				if(rightElement.matches("R[0-9]+")){
					int result = this.getElement(rightElement);
					this.Registers[leftElement] = result;
					//go to next line
					this.currentLine++;
					return "Set Register " + leftElement + " to " + result + ".";
				}
				//if the right element is an indirect register
				if(rightElement.matches("[(]R[0-9]+[)]")){
					int result = this.getElement(rightElement);
					this.Registers[leftElement] = result;
					//go to next line
					this.currentLine++;
					return "Set Register " + leftElement + " to " + result + ".";
				}
				//TODO: define more
				if(currentContent[1].matches(".*\\+.*")){
					String[] rightHalfs = currentContent[1].split("\\+");
					//check what the left part of the right element is
					int rightLeftHalf = getElement(rightHalfs[0]);
					//check what the right part of the right element is
					int rightRightHalf = getElement(rightHalfs[1]);
					this.Registers[leftElement] = rightLeftHalf + rightRightHalf;
					//go to next line
					this.currentLine++;
					return "Set Register " + leftElement + " to " + (rightLeftHalf + rightRightHalf) + ".";
				}
				if(currentContent[1].matches(".*\\*.*")){
					String[] rightHalfs = currentContent[1].split("\\*");
					rightHalfs[0] = rightHalfs[0];
					rightHalfs[1] = rightHalfs[1];
					//check what the left part of the right element is
					int rightLeftHalf = getElement(rightHalfs[0]);
					//check what the right part of the right element is
					int rightRightHalf = getElement(rightHalfs[1]);
					this.Registers[leftElement] = rightLeftHalf * rightRightHalf;
					this.lcmOrderTotal += this.lcmOrder;
					//go to next line
					this.currentLine++;
					return "Set Register " + leftElement + " to " + (rightLeftHalf * rightRightHalf) + ".";
					
				}
				if(currentContent[1].matches(".*/.*")){
					String[] rightHalfs = currentContent[1].split("/");
					rightHalfs[0] = rightHalfs[0];
					rightHalfs[1] = rightHalfs[1];
					//check what the left part of the right element is
					int rightLeftHalf = getElement(rightHalfs[0]);
					//check what the right part of the right element is
					int rightRightHalf = getElement(rightHalfs[1]);
					this.Registers[leftElement] = rightLeftHalf / rightRightHalf;
					this.lcmOrderTotal += this.lcmOrder;
					//go to next line
					this.currentLine++;
					return "Set Register " + leftElement + " to " + (rightLeftHalf / rightRightHalf) + ".";
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
					this.Registers[leftElement] = rightLeftHalf - rightRightHalf;
					this.lcmOrderTotal += this.lcmOrder;
					//go to next line
					this.currentLine++;
					return "Set Register " + leftElement + " to " + (rightLeftHalf - rightRightHalf) + ".";
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			return "Register(s) missing in line " + (this.currentLine + 1);
		}
		if(computingLine.matches("GGZ(R[0-9]+|[(]R[0-9]+[)]|[0-9]+),[L][0-9]+")){
			this.ucmOrder++;
			this.ucmOrderTotal++;
			this.ucmMemory++;
			this.ucmMemoryTotal++;
			String[] elements = computingLine.replace("GGZ", "").split(",");
			int result = this.getElement(elements[0]);
			this.lcmOrder = this.calculateLogarithmicCost(result);
			this.lcmOrderTotal += this.lcmOrder;
			if(result > 0){
				this.currentLine = this.labels.get(elements[1]);
				return elements[0] + " > 0" + ". Go to line " + this.currentLine + ".";
			}
			else{
				this.currentLine++;
				return elements[0] + " <= 0" + ". Just going one line further.";
			}
		}
		if(computingLine.matches("GLZ(R[0-9]+|[(]R[0-9]+[)]|[0-9]+),[L][0-9]+")){
			this.ucmOrder++;
			this.ucmOrderTotal++;
			this.ucmMemory++;
			this.ucmMemoryTotal++;
			String[] elements = computingLine.replace("GLZ", "").split(",");
			int result = this.getElement(elements[0]);
			this.lcmOrder = this.calculateLogarithmicCost(result);
			this.lcmOrderTotal += this.lcmOrder;
			if(result < 0){
				this.currentLine = this.labels.get(elements[1]);
				return elements[0] + " < 0" + ". Go to line " + this.currentLine + ".";
			}
			else{
				this.currentLine++;
				return elements[0] + " > 0" + ". Just going one line further.";
			}
		}
		if(computingLine.matches("GZ(R[0-9]+|[(]R[0-9]+[)]|[0-9]+),[L][0-9]+")){
			this.ucmOrder++;
			this.ucmOrderTotal++;
			this.ucmMemory++;
			this.ucmMemoryTotal++;
			String[] elements = computingLine.replace("GZ", "").split(",");
			int result = this.getElement(elements[0]);
			this.lcmOrder = this.calculateLogarithmicCost(result);
			this.lcmOrderTotal += this.lcmOrder;
			if(result == 0){
				this.currentLine = this.labels.get(elements[1]);
				return elements[0] + " == 0" + ". Go to line " + this.currentLine + ".";
			}
			else{
				this.currentLine++;
				return elements[0] + " != 0" + ". Just going one line further.";
			}
		}
		//if there is a GOTO just set the currentLine to the one which has the label
		if(computingLine.matches("GOTO[L][0-9]+")){
			this.ucmOrder++;
			this.ucmOrderTotal++;
			String nextLabel = computingLine.replace("GOTO", "");
			int nextLine = this.labels.get(nextLabel);
			this.currentLine = nextLine;
			return "Going to label " + nextLabel + " in line " + (nextLine + 1) + ".";
		}
		return "No possiblity to compute further. Is the syntax correct?";
	}
	
	//checks a halve of an assignment and returns it's value
	private int getElement(String half){
		//indirect register
		if(half.matches("[(].*[)]")){
			this.ucmMemory+=2;
			this.ucmMemoryTotal+=2;
			int number = Integer.parseInt(half.replace("(", "").replace(")", "").replace("R", ""));
			this.lcmOrder += (this.calculateLogarithmicCost(number + this.Registers[number] + this.Registers[this.Registers[number]]));
			int result = this.Registers[this.Registers[number]];
			return result;
		}
		//register
		if(half.matches("R[0-9]+")){
			this.ucmMemory++;
			this.ucmMemoryTotal++;
			int number = Integer.parseInt(half.replace("R", ""));
			this.lcmOrder += this.calculateLogarithmicCost(number + this.Registers[number]);
			int result = this.Registers[number];
			return result;
		}
		//constant
		if(half.matches("[0-9]+")){
			int number = Integer.parseInt(half);
			this.lcmOrder += this.calculateLogarithmicCost(number);
			return number;
		}
		//negative constant
		if(half.matches("\\-[0-9]+")){
			int number = Integer.parseInt(half.replace("-", ""));
			this.lcmOrder += this.calculateLogarithmicCost(number * -1);
			return Integer.parseInt(half);
		}
		return 0;
	}
	
	//runs through the program and in this way checks the syntax of the program
	//returns if there are errors
	boolean checkSyntax(){
		boolean result = true;
		//save the current line to set it to this point at the end again
		int initialCurrentLine = this.currentLine;
		this.currentLine = 0;
		String output = "";
		String lastOutput = "";
		if(this.lines.length > 0){
			lastOutput = this.computeLine();
			output = lastOutput;
		}
		while(this.currentLine < this.lines.length){
			lastOutput = this.computeLine();
			output = output.concat(System.getProperty("line.separator") + lastOutput);
			this.currentLine++;
			//if there are errors remember that, change the result and save the error message
			if(lastOutput.equals("Error. End of program reached, but HALT is missing.") || lastOutput.equals("No possiblity to compute further. Is the syntax correct?")){
				result = false;
				if(this.syntaxErrors != ""){
					this.syntaxErrors = this.syntaxErrors.concat(System.getProperty("line.separator") + "Line " + this.currentLine + ": " + lastOutput);
				}
				else{
					this.syntaxErrors = this.syntaxErrors.concat("Line " + this.currentLine + ": " + lastOutput);
				}
			}
		}
		this.currentLine = initialCurrentLine;
		return result;
	}
	
	String getSyntaxErrors(){
		return this.syntaxErrors;
	}
	
	//load a new set of registers and program and sets these to be the new registers and program to be computed
	void load(String newProgram, String newRegisters){
		this.setProgramAndRegisters(newProgram, newRegisters);
	}
	
	//returns the cost of the current line in the unified cost measure
	int[] getCurrentUnifiedCost(){
		int[] toReturn = {this.ucmOrder, this.ucmMemory};
		return toReturn;
	}
	
	//returns the total cost in the unified cost measure
	int[] getTotalUnifiedCost(){
		int[] toReturn = {this.ucmOrderTotal, this.ucmMemoryTotal};
		return toReturn;
	}
	
	//returns the cost of the current line in the logarithmic cost measure
	int getCurrentLogarithmicCost(){
		return (int)this.lcmOrder;
	}
	
	//returns the total cost in the logarithmic cost measure
	int getTotalLogarithmicCost(){
		return (int)this.lcmOrderTotal;
	}
	
	//calcualtes the logarithm of a given register to base 2, by using two natural logarithm calculations
	double calculateLogarithmicCost(int value){
		double cost = Math.log1p(Math.abs(value))/Math.log1p(2);
		return cost;
	}
}
