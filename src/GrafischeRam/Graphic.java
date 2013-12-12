package GrafischeRam;

import GrafischeRam.Ram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	private JPanel programPanel;		//contains the program
	private JTextArea programArea;
	private JPanel progressPanel;		//contains the compute buttons
	private JButton computeLine;
	private JButton computeAll;
	//private JPanel outputPanel;			//contains the output
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
		//create a text field for every register, put the content of the register into it, put it into the register panel and all together in the main frame
		this.registerPanel = new JPanel(new FlowLayout());
		JTextField[] registersFields = new JTextField[ram.Registers.length];
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
		JTextPane textPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(textPane);
		TextLineNumber tln = new TextLineNumber(textPane);
		scrollPane.setRowHeaderView( tln );
		this.programPanel.add(scrollPane);
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
		//show the  finished window
		this.frame.setVisible(true);
	}
	
	void load(String filename){
		BufferedReader file = null;
		try{
			file = new BufferedReader(new FileReader(filename));
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		String newRegisters = "";
		String newProgram = "";
		try{
			newRegisters = file.readLine();
			while(file.ready()){
				newProgram = newProgram.concat(file.readLine() + System.getProperty("line.separator"));
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		this.ram.load(newProgram, newRegisters);
	}
	
	void setOutput(String output){
		this.output.setText(output);
		frame.repaint();
	}
	
	private String[] chooseProgramFile(){
		this.fc.showOpenDialog(this.loadProgram);
		File file = fc.getSelectedFile();
		FileReader fileToRead = null;
		String textContainer = null;
		StringBuffer lesePuffer = new StringBuffer();
		try{
			fileToRead = new FileReader(file.getPath());
			BufferedReader in = new BufferedReader(fileToRead);
			try{
				while((textContainer = in.readLine()) != null){
					lesePuffer.append(textContainer + System.getProperty("line.separator"));
				}
				textContainer = new String(lesePuffer).substring(0, lesePuffer.length()-1);
				in.close();
			}
			catch(IOException e1){
				System.out.println("Read error " + e1);
			}
		}
		catch(IOException e2){
			System.out.println("Open error " + e2);
		}
		return textContainer.split(System.getProperty("line.separator"));
	}
	
	private boolean loadNewProgram(String[] program){
		//create the new program by concating the lines of the program and ignoring the first line 
		String newProgram = "";
		for(int i = 1; i < program.length; i++){
			newProgram = newProgram.concat(program[i] + System.getProperty("line.separator"));
		}
		this.ram.load(newProgram, program[0]);
		this.programArea.insert(newProgram, 0);
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
			case "load":
				loadNewProgram(chooseProgramFile());
				break;
			case "exit":
				System.exit(0);
		}
	}
}
