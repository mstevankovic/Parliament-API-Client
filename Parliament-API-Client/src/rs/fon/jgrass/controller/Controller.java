package rs.fon.jgrass.controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import rs.fon.jgrass.view.ParliamentAPIClientGUI;

public class Controller {

	/**
	 * 
	 */
	public static void closeTheApplication() {
		int option = JOptionPane.showConfirmDialog(
				null,
				"Quit this application???",
				"Exit",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (option == JOptionPane.YES_OPTION)
			System.exit(0);	
	}
	
	/**
	 * Method for displaying ParliamentAPIClientGUI
	 */
	public static void showParliamentAPIClientGUI() {
		ParliamentAPIClientGUI frame = new ParliamentAPIClientGUI();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public static void showAbouDialog() {
		JOptionPane.showMessageDialog(
				null,
				"Author: Marko Stevankovic",
				"About",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void fillTable() {
		// TODO Auto-generated method stub
		
	}

	public static void getMembers() {
		// TODO Auto-generated method stub
		
	}

	public static void updateMembers() {
		// TODO Auto-generated method stub
		
	}

}
