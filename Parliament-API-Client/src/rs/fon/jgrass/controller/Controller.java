package rs.fon.jgrass.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import rs.fon.jgrass.domain.Member;
import rs.fon.jgrass.util.ParliamentAPICommunication;
import rs.fon.jgrass.view.ParliamentAPIClientGUI;
import rs.fon.jgrass.view.table_model.MemberTableModel;

public class Controller {

	public static void getMembers() {
		ParliamentAPICommunication instance = ParliamentAPICommunication.getInstance();
		instance.saveMembersToFile();
		Controller.messageToStatusBar("Members successfully loaded.");
	}

	public static void fillTable() {
		ParliamentAPICommunication instance = ParliamentAPICommunication.getInstance();
		instance.loadMembersFromFile("data/serviceMembers.json");
		refreshTable();
		messageToStatusBar("Table filled with data loaded from service.");
	}

	public static void updateMembers() {
		ParliamentAPICommunication instance = ParliamentAPICommunication.getInstance();
		
		MemberTableModel model = (MemberTableModel)ParliamentAPIClientGUI.getInstance().getTable().getModel();
		List<Member> tableMembers = model.getMembers();
		
		JsonArray membersArray = instance.serializeMembers(tableMembers);

		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter("data/updatedMembers.json")));
		} catch (IOException exc) {
			Controller.showErrorDialog(exc);
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String membersString = gson.toJson(membersArray);

		out.println(membersString);
		out.close();
		messageToStatusBar("Updated members succesfully saved!");
	}

	private static void messageToStatusBar(String message) {
  		ParliamentAPIClientGUI instance = ParliamentAPIClientGUI.getInstance();
  		instance.getTextArea().append(message + "\n");
  	}
	
	private static void refreshTable(){
		MemberTableModel model = (MemberTableModel)ParliamentAPIClientGUI.getInstance().getTable().getModel();
		model.fireTableDataChanged();
	}
	
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

	public static void showAbouDialog() {
		JOptionPane.showMessageDialog(
  				null,
 				"Author: Marko Stevankovic",
 				"About",
 				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showParliamentAPIClientGUI() {
		ParliamentAPIClientGUI instance = ParliamentAPIClientGUI.getInstance();
		instance.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		instance.setVisible(true);
		instance.setLocationRelativeTo(null);
	}
	
	public static void showErrorDialog(Exception exc){
		JOptionPane.showMessageDialog(
				null,
				exc.getMessage(), 
				"ERROR", 
				JOptionPane.ERROR_MESSAGE);
	}

	public static void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(
				null,
				message,
				"ERROR", 
				JOptionPane.ERROR_MESSAGE);
	}
	
}
