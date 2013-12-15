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
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
	private JButton newRegister;
	private int nrRegisters;
	private JPanel programPanel;		//contains the program
	private JTextArea programArea;
	private JPanel progressPanel;		//contains the compute buttons
	private JButton computeLine;
	private JButton computeAll;
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
		JTextField[] registersFields = new JTextField[ram.Registers.length];
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
		//create two buttons, the first to compute one line, the second to compute all possible...
		this.computeLine = new JButton("compute line");
		this.computeAll = new JButton("compute all");
		//and add both to the progressPanel, which is added to the main frame
		this.progressPanel = new JPanel(new FlowLayout());
		this.progressPanel.add(this.computeLine);
		this.progressPanel.add(this.computeAll);
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
	
	//puts the given output into the text field for it
	void setOutput(String output){
		this.output.setText(output);
		//this.frame.revalidate();
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
		this.ram.load(newProgram, program[0]);
		System.out.println("newProgram:\n" + newProgram);
		this.programArea.setText(newProgram);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
			case "load":
				String[] newProgramLines = chooseProgramFile();
				if(newProgramLines != null){
					loadNewProgram(newProgramLines);
				}
				break;
			case "exit":
				System.exit(0);
				break;
			case "newRegister":
				JTextField newRegisterField = new JTextField(5);
				newRegisterField.setText("0");
				this.registerPanel.add(newRegisterField);
				this.nrRegisters++;
				if(this.ram.Registers.length < this.nrRegisters){
					this.ram.addNewRegister();
				}
				this.frame.revalidate();
				break;
		}
	}
}
