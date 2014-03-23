package GrafischeRam;

import GrafischeRam.Ram;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class GraphicSwing implements ActionListener{
	private Ram ram;
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenuItem saveProgram;
	private JMenuItem loadProgram;
	private JMenuItem exit;
	private JPanel registerPanel;			//contains the panel with the registers and the button to create new registers
	private JPanel registersPanel;			//contains the registers
	private JScrollPane registerScrollPane;
	private JTextField[] registersFields;
	private JButton newRegisterButton;
	private int nrRegisters;
	private JScrollPane programPanel;		//contains the program
	private JTextArea programArea;
	private JPanel costMeasurePanel;
	private JLabel ucmLabel;
	private JLabel ucmOrder;
	private JLabel ucmMemory;
	private JLabel ucmTotalLabel;
	private JLabel ucmOrderTotal;
	private JLabel ucmMemoryTotal;
	private JLabel lcmLabel;
	private JLabel lcmOrder;
	private JLabel lcmMemory;
	private JLabel lcmOrderTotal;
	private JLabel lcmMemoryTotal;
	private JLabel lcmTotalLabel;
	private JPanel progressPanel;			//contains the compute buttons
	private JPanel buttonPanel;
	private JButton resetButton;
	private JPanel currentLine;
	private JButton computeLineButton;
	private JButton computeProgramButton;
	private JTextField output;
	private String lastProgram;
	private int[] lastRegisters;
	private String savedProgram;
	final JFileChooser fc = new JFileChooser();
	
	public GraphicSwing(Ram ram){
		this.ram = ram;
		//create the main frame and set it's default values
		this.frame = new JFrame("Graphical Ram");
		this.frame.setSize(800,600);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		//create the menu bar and it's elements
		this.menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		this.loadProgram = new JMenuItem("Load Program");
		this.loadProgram.setActionCommand("load");
		this.loadProgram.addActionListener(this);
		fileMenu.add(loadProgram);
		this.saveProgram = new JMenuItem("Save Program");
		this.saveProgram.setActionCommand("save");
		this.saveProgram.addActionListener(this);
		fileMenu.add(saveProgram);
		this.exit = new JMenuItem("Exit");
		this.exit.setActionCommand("exit");
		this.exit.addActionListener(this);
		fileMenu.add(this.exit);
		this.menuBar.add(fileMenu);
		this.frame.setJMenuBar(this.menuBar);
		//create the panel for the registers
		this.registerPanel = new JPanel(new BorderLayout());
		//create a button to add more registers
		this.newRegisterButton = new JButton("Add Register");
		this.newRegisterButton.setActionCommand("newRegister");
		this.newRegisterButton.addActionListener(this);
		this.registerPanel.add(this.newRegisterButton, BorderLayout.WEST);
		//create a panel separately for the registers
		this.registersPanel = new JPanel(new FlowLayout());
		//create a text field for every register, put the content of the register into it, put it into the register panel and all together in the main frame
		registersFields = new JTextField[ram.Registers.length];
		this.nrRegisters = ram.Registers.length;
		for(int i = 0; i < ram.Registers.length; i++){
			this.registersFields[i] = new JTextField(5);
			this.registersFields[i].setText(String.valueOf(ram.Registers[i]));
			this.registersFields[i].setToolTipText("R" + i);
			this.registersPanel.add(registersFields[i]);
		}
		this.registerPanel.add(registersPanel, BorderLayout.CENTER);
		//put the panel for the registers and the add button into a scrollpane to enable scrolling
		this.registerScrollPane = new JScrollPane(this.registerPanel);
		this.registerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		this.registerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.frame.add(this.registerScrollPane, BorderLayout.NORTH);
		//create the text area for the programm and put it into the programm panel which contains scroll bars
		this.programArea = new JTextArea(25, 50);
		this.programArea.setEditable(true);
		this.programPanel = new JScrollPane(this.programArea);
		this.frame.add(this.programPanel, BorderLayout.CENTER);
		this.showCurrentLine();
		//create  the panel for the cost measure
		this.costMeasurePanel = new JPanel(new GridLayout(0, 3, 10, 0));
		this.costMeasurePanel.add(new JLabel(" "));
		this.costMeasurePanel.add(new JLabel("Order"));
		this.costMeasurePanel.add(new JLabel("Memory"));
		
		this.ucmLabel = new JLabel("UCM");
		this.costMeasurePanel.add(this.ucmLabel);
		this.ucmOrder = new JLabel("0");
		this.costMeasurePanel.add(this.ucmOrder);
		this.ucmMemory = new JLabel("0");
		this.costMeasurePanel.add(this.ucmMemory);
		
		this.ucmTotalLabel = new JLabel("Total UCM");
		this.costMeasurePanel.add(this.ucmTotalLabel);
		this.ucmOrderTotal = new JLabel("0");
		this.costMeasurePanel.add(this.ucmOrderTotal);
		this.ucmMemoryTotal = new JLabel("0");
		this.costMeasurePanel.add(this.ucmMemoryTotal);
		
		this.lcmLabel = new JLabel("LCM");
		this.costMeasurePanel.add(this.lcmLabel);
		this.lcmOrder = new JLabel("0");
		this.costMeasurePanel.add(this.lcmOrder);
		this.lcmMemory = new JLabel("-");
		this.costMeasurePanel.add(this.lcmMemory);
		
		this.lcmTotalLabel = new JLabel("Total LCM");
		this.costMeasurePanel.add(this.lcmTotalLabel);
		this.lcmOrderTotal = new JLabel("0");
		this.costMeasurePanel.add(this.lcmOrderTotal);
		this.lcmMemoryTotal = new JLabel("-");
		this.costMeasurePanel.add(this.lcmMemoryTotal);
		
		this.frame.add(this.costMeasurePanel, BorderLayout.EAST);
		//create three buttons, the first to reset the program, the second to compute one line, the third to compute all possible...
		this.resetButton = new JButton("Reset program");
		this.resetButton.setEnabled(false);
		this.resetButton.addActionListener(this);
		this.resetButton.setActionCommand("reset");
		this.computeLineButton = new JButton("Compute Current Line");
		this.computeLineButton.addActionListener(this);
		this.computeLineButton.setActionCommand("computeLine");
		this.computeProgramButton = new JButton("Compute Program");
		this.computeProgramButton.addActionListener(this);
		this.computeProgramButton.setActionCommand("computeProgram");
		//and add both to the progressPanel, which is added to the main frame
		this.progressPanel = new JPanel(new BorderLayout());
		//create a panel for the buttons, add them and then add the button panel to the progress panel
		this.buttonPanel = new JPanel(new FlowLayout());
		this.buttonPanel.add(this.resetButton);
		this.buttonPanel.add(this.computeLineButton);
		this.buttonPanel.add(this.computeProgramButton);
		this.progressPanel.add(buttonPanel, BorderLayout.WEST);
		//create the text field for the output and make it uneditable
		this.output = new JTextField(50);
		this.output.setEditable(false);
		this.progressPanel.add(this.output, BorderLayout.CENTER);
		//create the panel for the output, put the text field for the output in it and put the panel in the frame
		this.frame.add(this.progressPanel, BorderLayout.SOUTH);
		//create a scroll pane so content not fitting in the window will be shown
		//show the  finished window
		this.frame.setVisible(true);
		this.frame.validate();
		this.savedProgram = this.ram.program;
		if(!this.ram.program.equals("")){
			this.programArea.setText(this.ram.program);
		}
		this.lastRegisters = new int[0];
		this.lastProgram = "";
	}
	
	//updates the registers shown by the window
	private void updateRegister(){
		//create a panel to show the registers
		if(this.registersPanel != null){
			this.registersPanel.removeAll();
		}
		else{
			this.registersPanel = new JPanel(new FlowLayout());
		}
		//create a text field for each register
		for(int i = 0; i < this.nrRegisters; i++){
			this.registersFields[i] = new JTextField(5);
			this.registersFields[i].setToolTipText("R" + i);
			this.registersPanel.add(registersFields[i]);
		}
		//fill the registers with values, but only those used by the ram
		for(int i = 0; i < this.ram.Registers.length; i++){
			this.registersFields[i].setText(String.valueOf(ram.Registers[i]));
		}
		this.registersPanel.repaint();
		this.registerPanel.add(this.registersPanel, BorderLayout.CENTER);
		this.frame.add(this.registerScrollPane, BorderLayout.NORTH);
		this.frame.revalidate();
	}
	
	//creates a panel that shows in which line the ram currently is in
	private void showCurrentLine(){
		//create a panel to show which line is currently worked on
		if(this.currentLine != null){
			this.currentLine.removeAll();
		}
		if(this.ram.currentLine == -1 && !this.programArea.getText().matches("")){
			
		}
		this.currentLine = new JPanel();
		this.currentLine.setLayout(new BoxLayout(this.currentLine, BoxLayout.PAGE_AXIS));
		for(int i = 0; i < this.ram.lines.length; i++){
			JLabel label;
			if(i != this.ram.currentLine){
				label = new JLabel(" ");
			}
			else{
				label = new JLabel("->");
			}
			this.currentLine.add(label);
		}
		this.frame.add(this.currentLine, BorderLayout.WEST);
		this.frame.revalidate();
	}
	//puts the given output into the text field for it
	void setOutput(String output){
		this.output.setText(output);
	}
	
	//opens a file chooser, read the content of the file and return the lines in a String array
	private String[] chooseProgramFile(){
		this.fc.showOpenDialog(this.loadProgram);
		File file = fc.getSelectedFile();
		FileReader fileToRead = null;
		String textContainer = null;
		if(file != null){
			StringBuffer readPuffer = new StringBuffer();
			try{
				fileToRead = new FileReader(file.getPath());
				BufferedReader in = new BufferedReader(fileToRead);
				try{
					while((textContainer = in.readLine()) != null){
						readPuffer.append(textContainer + "%");
					}
					textContainer = new String(readPuffer).substring(0, readPuffer.length()-1);
					in.close();
				}
				catch(IOException e1){
					System.out.println("Read error " + e1);
				}
				catch(StringIndexOutOfBoundsException e2){
					//if the file is empty return null, so nothing will be loaded
					return null;
				}
			}
			catch(IOException e3){
				System.out.println("Open error " + e3);
			}
			return textContainer.split("%");
		}
		return null;
	}
	
	//load a new program given (usually by chooseProgramFile), gives it to the Ram and puts the text into the program area
	private void loadNewProgram(String[] program, boolean remember){
		//create the new program by concating the lines of the program and ignoring the first line 
		String newProgram = "";
		for(int i = 1; i < program.length; i++){
			newProgram = newProgram.concat(program[i] + System.getProperty("line.separator"));
		}
		//get the registers from the file
		String[] registers = program[0].split(";");
		//if there are more registers in the GUI, remove them, i. e. create a new panel with less
		if(registers.length < this.nrRegisters){
			//create a panel to show the registers
			if(this.registersPanel != null){
				this.registersPanel.removeAll();
			}
			else{
				this.registersPanel = new JPanel(new FlowLayout());
			}
			for(int i = 0; i < registers.length; i++){
				registersFields[i] = new JTextField(5);
				registersFields[i].setText(String.valueOf(registers[i]));
				registersFields[i].setToolTipText("R" + i);
				this.registersPanel.add(registersFields[i]);
			}
			//create a new set of registers with the correct size
			JTextField[] tmp = new JTextField[registers.length];
			//put the values from the old registers into the new ones and link it to registersFields
			for(int i = 0; i < registers.length; i++){
				tmp[i] = this.registersFields[i];
			}
			this.registersFields = tmp;
			//put the panels into the upper elements
			this.registerPanel.add(this.registersPanel, BorderLayout.CENTER);
			this.registerPanel.repaint();
			this.frame.add(this.registerScrollPane, BorderLayout.NORTH);
			this.frame.revalidate();
			this.nrRegisters = registers.length;
		}
		else{
			for(int i = 0; i < this.registersFields.length; i++){
				registersFields[i].setText(registers[i]);
			}
			//if the loaded file contains more registers create them
			if(registers.length > this.nrRegisters){
				for(int i = this.registersFields.length; i < registers.length; i++){
					addRegister(registers[i]);
				}
			}
		}
		//pass the program and the registers to the ram
		this.ram.load(newProgram.replaceAll("(?<!\r)\n", System.getProperty("line.separator")), program[0]);
		//put the program in the program area
		this.programArea.setText(newProgram);
		if(remember) this.savedProgram = newProgram;
		//save the registers for reseting
		this.lastRegisters = new int[registers.length];
		for(int i = 0; i < registers.length; i++){
			this.lastRegisters[i] = Integer.parseInt(registers[i].trim());
		}
		this.showCurrentLine();
		this.resetButton.setEnabled(true);
	}
	
	private void saveProgram(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.showSaveDialog(null);
	    if(fileChooser.getSelectedFile() != null){
	    	File selFile = fileChooser.getSelectedFile();
		    String outputText = getRegisters() + System.getProperty("line.separator") + this.programArea.getText();
			FileOutputStream fileOut = null;
			try{
				fileOut = new FileOutputStream(selFile.getPath());
			}
			catch(FileNotFoundException e1){
				e1.printStackTrace();
			}
			try{
				fileOut.write(outputText.getBytes(), 0, outputText.length());
				fileOut.close();
			}
			catch(IOException e2){
				e2.printStackTrace();
			}
	    }
	}
	
	//adds a new register with the given content, also updates the registers of the ram
	private void addRegister(String content){
		JTextField[] tmp = new JTextField[this.registersFields.length + 1];
		//put the values from the old registers into the new ones
		for(int i = 0; i < this.registersFields.length; i++){
			tmp[i] = this.registersFields[i];
		}
		//create a new text field
		JTextField newRegisterField = new JTextField(5);
		newRegisterField.setText(content);
		tmp[this.registersFields.length] = newRegisterField;
		//link to the new registers
		this.registersFields = tmp;
		//add the registers to the frame
		for(int i = 0; i < this.registersFields.length; i++){
			this.registersPanel.add(this.registersFields[i]);
			registersFields[i].setToolTipText("R" + i);
		}
		this.registerPanel.add(this.registersPanel, BorderLayout.CENTER);
		this.nrRegisters++;
		this.ram.Registers = new int[this.nrRegisters];
		this.frame.revalidate();
	}
	
	private String getRegisters(){
		String registers = this.registersFields[0].getText();
		//get all the contents of the registers and create a string that contains them is separated by semicolons, except the first, because it should not start with semicolon
		for(int i = 1; i < this.registersFields.length; i++){
			registers = registers.concat(";" + this.registersFields[i].getText());
		}
		return registers;
	}
	
	//resets the program from the window and takes the content from the registers and gives it to the underlying ram
	private void resetProgram(){
		String newRegisters = "";
		for(int i = 0; i < this.lastRegisters.length-1; i++){
			newRegisters = newRegisters.concat(this.lastRegisters[i] + ";");
		}
		newRegisters = newRegisters.concat(String.valueOf(this.lastRegisters[this.lastRegisters.length-1]));
		//combine the register string and the program to an array and give it to the ram by passing it to loadNewProgram
		//String[] newProgram = this.programArea.getText().split(System.getProperty("line.separator"));
		String[] newProgram = this.savedProgram.split(System.getProperty("line.separator"));
		String[] inputToLoad = new String[newProgram.length + 1];
		inputToLoad[0] = newRegisters;
		for(int i = 0; i < newProgram.length; i++){
			inputToLoad[i+1] = newProgram[i];
		}
		this.loadNewProgram(inputToLoad, false);
	}
	
	private void reloadProgram(){
		String newRegisters = this.registersFields[0].getText();
		try{
			for(int i = 1; i < this.registersFields.length; i++){
				newRegisters = newRegisters.concat(";" + this.registersFields[i].getText());
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			String newProgram = this.programArea.getText().replaceAll("(?<!\r)\n", System.getProperty("line.separator"));
			this.ram.load(newProgram, newRegisters);
			throw e;
		}
		String newProgram = this.programArea.getText().replaceAll("(?<!\r)\n", System.getProperty("line.separator"));
		this.ram.load(newProgram, newRegisters);
	}
	
	//checks all registers if there are changes and pushes the changes to the Ram
	private void checkRegisters(){
		for(int i = 0; i < this.registersFields.length; i++){
			if(this.ram.Registers[i] != Integer.parseInt(this.registersFields[i].getText())){
				this.ram.Registers[i] = Integer.parseInt(this.registersFields[i].getText());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
			case "load":
				String[] newProgramLines = chooseProgramFile();
				if(newProgramLines != null){
					loadNewProgram(newProgramLines, true);
				}
				this.output.setText("");
				break;
			case "save":
				saveProgram();
			    break;
			case "exit":
				System.exit(0);
				break;
			case "newRegister":
				this.addRegister("0");
				break;
			case "reset":
				this.resetProgram();
				this.updateRegister();
				showCurrentLine();
				break;
			case "computeLine":
				this.computeLineButton.setEnabled(false);
				checkRegisters();
				try{
					//check if program has been changed and reload if necessary
					if(!this.lastProgram.equals(this.programArea.getText())){
						this.reloadProgram();
					}
					this.output.setText((this.ram.computeLine()));
					this.updateRegister();
					this.lastProgram = this.programArea.getText();
					showCurrentLine();
					//get the unified costs of the current line and display them
					int[] unifiedCost = this.ram.getCurrentUnifiedCost();
					this.ucmOrder.setText(String.valueOf(unifiedCost[0]));
					this.ucmMemory.setText(String.valueOf(unifiedCost[1]));
					this.lcmOrder.setText(String.valueOf(this.ram.getCurrentLogarithmicCost()));
					//get the total unified costs and display them
					int[] unifiedTotalCost = this.ram.getTotalUnifiedCost();
					this.ucmOrderTotal.setText(String.valueOf(unifiedTotalCost[0]));
					this.ucmMemoryTotal.setText(String.valueOf(unifiedTotalCost[1]));
					this.lcmOrderTotal.setText(String.valueOf(this.ram.getTotalLogarithmicCost()));
				}
				catch(ArrayIndexOutOfBoundsException exception){
					this.output.setText("At least one register in line " + (this.ram.currentLine + 1)  + " does not exist");
				}
				this.computeLineButton.setEnabled(true);
				break;
			case "computeProgram":
				this.computeProgramButton.setEnabled(false);
				this.computeLineButton.doClick();
				while(!this.output.getText().matches("No possiblity to compute further.*") && !this.output.getText().matches("End of program reached.*") && ! this.output.getText().matches("Syntax not allowed.*")){
					this.computeLineButton.doClick();
				}
				this.computeProgramButton.setEnabled(true);
				break;
		}
	}
}