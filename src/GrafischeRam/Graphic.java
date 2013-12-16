package GrafischeRam;

import GrafischeRam.Ram;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
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
import javax.swing.JTextPane;

public class Graphic implements ActionListener{
	private Ram ram;
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenuItem loadProgram;
	private JPanel registerPanel;		//contains the register fields and their values
	private JTextField[] registersFields;
	private JButton newRegister;
	private int nrRegisters;
	private JPanel programPanel;		//contains the program
	private JTextArea programArea;
	private JPanel progressPanel;		//contains the compute buttons
	private JPanel currentLine;
	private JButton computeLine;
	private JButton computeProgram;
	private JTextField output;
	final JFileChooser fc = new JFileChooser();
	
	public Graphic(Ram ram){
		this.ram = ram;
		//create the main frame and set it's default values
		frame = new JFrame("Graphical Ram");
		frame.setSize(800,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		//create the menu bar and it's elements
		menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		loadProgram = new JMenuItem("Load program");
		loadProgram.setActionCommand("load");
		loadProgram.addActionListener(this);
		fileMenu.add(loadProgram);
		JMenuItem exit = new JMenuItem("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(this);
		fileMenu.add(exit);
		this.menuBar.add(fileMenu);
		this.frame.setJMenuBar(this.menuBar);
		//create the panel for the registers
		this.registerPanel = new JPanel(new FlowLayout());
		//create a button to add more registers
		this.newRegister = new JButton("add Register");
		this.newRegister.setActionCommand("newRegister");
		this.newRegister.addActionListener(this);
		this.registerPanel.add(this.newRegister);
		//create a text field for every register, put the content of the register into it, put it into the register panel and all together in the main frame
		registersFields = new JTextField[ram.Registers.length];
		this.nrRegisters = ram.Registers.length;
		for(int i = 0; i < ram.Registers.length; i++){
			registersFields[i] = new JTextField(5);
			registersFields[i].setText(String.valueOf(ram.Registers[i]));
			this.registerPanel.add(registersFields[i]);
		}
		this.frame.add(this.registerPanel, BorderLayout.NORTH);
		//create the text area for the programm and put it into the programm panel
		this.programArea = new JTextArea(25, 50);
		this.programArea.setEditable(true);
		this.programPanel = new JPanel(new BorderLayout());
		this.programPanel.add(programArea, BorderLayout.CENTER);
		//create the labels which show the lines
		//JTextPane textPane = new JTextPane();
		//JScrollPane scrollPane = new JScrollPane(textPane);
		//TextLineNumber tln = new TextLineNumber(textPane);
		//scrollPane.setRowHeaderView( tln );
		//this.programPanel.add(scrollPane);
		this.frame.add(this.programPanel, BorderLayout.CENTER);
		this.showCurrentLine();
		//create two buttons, the first to compute one line, the second to compute all possible...
		this.computeLine = new JButton("compute current line");
		this.computeLine.addActionListener(this);
		this.computeLine.setActionCommand("computeLine");
		this.computeProgram = new JButton("compute program");
		this.computeProgram.addActionListener(this);
		this.computeProgram.setActionCommand("computeProgram");
		//and add both to the progressPanel, which is added to the main frame
		this.progressPanel = new JPanel(new FlowLayout());
		this.progressPanel.add(this.computeLine);
		this.progressPanel.add(this.computeProgram);
		//create the text field for the output and make it uneditable
		this.output = new JTextField(20);
		this.output.setEditable(false);
		this.progressPanel.add(this.output);
		//create the panel for the output, put the text field for the output in it and put the panel in the frame
		this.frame.add(this.progressPanel, BorderLayout.SOUTH);
		//create a scroll pane so content not fitting in the window will be shown
		//show the  finished window
		this.frame.setVisible(true);
		this.frame.validate();
	}
	
	private void updateRegister(){
		//create a panel to show the registers
		if(this.registerPanel != null){
			this.registerPanel.removeAll();
			this.registerPanel.add(this.newRegister);
		}
		for(int i = 0; i < this.nrRegisters; i++){
			registersFields[i] = new JTextField(5);
			registersFields[i].setText(String.valueOf(ram.Registers[i]));
			this.registerPanel.add(registersFields[i]);
		}
		this.frame.add(this.registerPanel, BorderLayout.NORTH);
		this.frame.revalidate();
	}
	
	//creates a panel that shows in which line the ram currently is in
	private void showCurrentLine(){
		//create a panel to show which line is currently worked on
		if(this.currentLine != null){
			this.currentLine.removeAll();
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
			}
			catch(IOException e2){
				System.out.println("Open error " + e2);
			}
			return textContainer.split("%");
		}
		return null;
	}
	
	//load a new program given (usually by chooseProgramFile), gives it to the Ram and puts the text into the program area
	private void loadNewProgram(String[] program){
		//create the new program by concating the lines of the program and ignoring the first line 
		String newProgram = "";
		for(int i = 1; i < program.length; i++){
			newProgram = newProgram.concat(program[i] + System.getProperty("line.separator"));
		}
		//get the registers from the file
		String[] registers = program[0].split(";");
		for(int i = 0; i < this.registersFields.length; i++){
			registersFields[i].setText(registers[i]);
		}
		//if the loaded file contains more registers create them
		if(registers.length > this.nrRegisters){
			for(int i = this.registersFields.length; i < registers.length; i++){
				addRegister(registers[i]);
			}
		}
		//pass the program and the registers to the ram
		this.ram.load(newProgram, program[0]);
		//put the program in the program area
		this.programArea.setText(newProgram);
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
			this.registerPanel.add(this.registersFields[i]);
		}
		this.nrRegisters++;
		this.frame.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
			case "load":
				String[] newProgramLines = chooseProgramFile();
				if(newProgramLines != null){
					loadNewProgram(newProgramLines);
				}
				this.output.setText("");
				break;
			case "exit":
				System.exit(0);
				break;
			case "newRegister":
				this.addRegister("0");
				break;
			case "computeLine":
				this.output.setText((this.ram.computeLine()));
				this.updateRegister();
				showCurrentLine();
				break;
			case "computeProgram":
				synchronized(this){
					String run = (this.ram.computeLine());
					this.updateRegister();
					this.output.setText(run);
					while(!run.matches("No possiblity to compute further.*") && !run.matches("End of program reached.*") && ! run.matches("Syntax not allowed.*")){
						run = (this.ram.computeLine());
						this.updateRegister();
						this.output.setText(run);
					}
				}
				break;
		}
	}
}
