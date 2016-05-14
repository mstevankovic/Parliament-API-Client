package rs.fon.jgrass.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.border.Border;

import rs.fon.jgrass.controller.Controller;
import rs.fon.jgrass.view.table_model.MemberTableModel;

public class ParliamentAPIClientGUI extends JFrame implements ActionListener {

	private JPanel panelEast;
		
	private static ParliamentAPIClientGUI instance;
		
	private JMenuBar menuBar;
		
	private JMenu fileMenu;
	private JMenu separatorMenu;
	private JMenu helpMenu;
		
	private JMenuItem itemGETMembers;
	private JMenuItem itemFillTable;
	private JMenuItem itemUpdateMembers;
	private JMenuItem itemExit;
	private JMenuItem itemAbout;
		
	private JTable table;
	
	private JScrollPane scrollPane;
	private JScrollPane scrollPaneTextArea;
		
	private JPopupMenu popUpMenu;
		
	private JTextArea textArea;
	
	private JButton buttonGETMembers;
	private JButton buttonFillTable;
	private JButton buttonUpdateMembers;
	
	private JMenuItem itemGET;
	private JMenuItem itemFILL;
	private JMenuItem itemUPDATE;
		
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
		
	/**
	 * Instatiating only once
	 * @return instance
	 */
	public static ParliamentAPIClientGUI getInstance(){
		if(instance == null){
			instance = new ParliamentAPIClientGUI();
		}
		return instance;
	}
	
	/**
	 * Private constructor
	 */
	private ParliamentAPIClientGUI()
	{	
		/**
		 * Defining listeners
		 */
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Controller.closeTheApplication();
			}
		});
		setTitle("Parliament members");
		setSize(WIDTH, HEIGHT);
		setLayout(new BorderLayout());
			
		panelEast = new JPanel(new FlowLayout(FlowLayout.CENTER));
			
		/*
		 * Adjusting menu bar
		 */
		menuBar = new JMenuBar();
			
		fileMenu = new JMenu("File");
		separatorMenu = new JMenu("|");
		helpMenu = new JMenu("Help");
			
		separatorMenu.setEnabled(false);
			
		itemGETMembers = new JMenuItem("GET members");
		itemFillTable = new JMenuItem("Fill table");
		itemUpdateMembers = new JMenuItem("Update members");
		itemExit = new JMenuItem("Exit");
			
		itemGETMembers.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_G, InputEvent.CTRL_MASK));
		itemFillTable.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_F, InputEvent.CTRL_MASK));
		itemUpdateMembers.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_U, InputEvent.CTRL_MASK));
		itemExit.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_X, InputEvent.ALT_MASK));
			
		itemExit.addActionListener(this);
		itemFillTable.addActionListener(this);
		itemUpdateMembers.addActionListener(this);
			itemGETMembers.addActionListener(this);
			
		itemAbout = new JMenuItem("About");
			
		itemAbout.addActionListener(this);
			
		fileMenu.add(itemGETMembers);
		fileMenu.add(itemFillTable);
		fileMenu.add(itemUpdateMembers);
		fileMenu.add(itemExit);
			
		helpMenu.add(itemAbout);
			
		menuBar.add(fileMenu);
		menuBar.add(separatorMenu);
		menuBar.add(helpMenu);
			
		setJMenuBar(menuBar);
			
		/*
		 * Adjusting central panel
		 */
		
		table = new JTable(new MemberTableModel());
			
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		scrollPane.setPreferredSize(new Dimension(WIDTH - 150, HEIGHT - HEIGHT / 10 - 10));
			
		itemGET = new JMenuItem("GET Members");
		itemFILL = new JMenuItem("Fill table");
		itemUPDATE = new JMenuItem("Update members");
			
		itemGET.addActionListener(this);
		itemFILL.addActionListener(this);
		itemUPDATE.addActionListener(this);
			
		popUpMenu = new JPopupMenu();
			
		popUpMenu.add(itemGET);
		popUpMenu.add(itemFILL);
		popUpMenu.add(itemUPDATE);
			
		table.setComponentPopupMenu(popUpMenu);
					
		/*
		 * Adjusting south panel
		 */
			
		textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(WIDTH - 2, HEIGHT / 10));
			
		scrollPaneTextArea = new JScrollPane(textArea);
		scrollPaneTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
		Border border = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder(border, "STATUS");
			
		scrollPaneTextArea.setBorder(titled);
			
		/*
		 * Adjusting east panel
		 */
		panelEast.setPreferredSize(new Dimension(150, HEIGHT - HEIGHT/10));
			
		buttonGETMembers = new JButton("GET Members");
		buttonFillTable = new JButton("Fill table");
		buttonUpdateMembers= new JButton("Update members");
			
		buttonGETMembers.addActionListener(this);
		buttonFillTable.addActionListener(this);
		buttonUpdateMembers.addActionListener(this);
			
		panelEast.add(buttonGETMembers);
		panelEast.add(buttonFillTable);
		panelEast.add(buttonUpdateMembers);
			
		/*
		 * Adding panels to frame...
		 */
			
		add(scrollPane, BorderLayout.CENTER);
		add(panelEast, BorderLayout.EAST);
		add(scrollPaneTextArea, BorderLayout.SOUTH);
			
		pack();
	}
		
	public JTable getTable() {
		return table;
	}
		
	public JTextArea getTextArea() {
		return textArea;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == itemAbout){
			Controller.showAbouDialog();
		} else if(source == itemExit){
			Controller.closeTheApplication();
		} else if(source == buttonFillTable || source == itemFillTable || source == itemFillTable){
			Controller.fillTable();
		} else if(source == buttonGETMembers || source == itemGET || source == itemGETMembers){
			Controller.getMembers();
		} else if(source == buttonUpdateMembers || source == itemUPDATE || source == itemUpdateMembers){
			Controller.updateMembers();
		}
	}
}