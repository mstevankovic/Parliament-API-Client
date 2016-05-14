package rs.fon.jgrass.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import rs.fon.jgrass.domain.Member;
import rs.fon.jgrass.model.CollectionOfMembers;
import rs.fon.jgrass.model.ParliamentAPICommunication;
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
		ParliamentAPIClientGUI instance = ParliamentAPIClientGUI.getInstance();
		instance.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		instance.setVisible(true);
		instance.setLocationRelativeTo(null);
	}

	public static void getMembers() {
		Controller.loadMembersIntoCollectionOfMembers();
		Controller.saveMembersToFile();
		
		String message = "Members successfully loaded.";
		Controller.messageToStatusBar(message);
	}

	public static void fillTable() {
		// TODO Auto-generated method stub
		
	}

	public static void updateMembers() {
		// TODO Auto-generated method stub
		
	}

	private static void loadMembersIntoCollectionOfMembers() {
		
		CollectionOfMembers instance = CollectionOfMembers.getInstance();
		instance.getListOfMembers().clear();
		
		ParliamentAPICommunication parliament = new ParliamentAPICommunication();
		List<Member> members = new ArrayList<>();
		
		try {
			members = parliament.getListOfMembers();
		} catch (Exception exc) {
			Controller.showErrorDialog(exc);
		}
		
		for(Member member : members){
			instance.addMember(member);
		}
	}
	
	private static void saveMembersToFile(){
		
		JsonArray membersJson = Controller.serializeMembers(CollectionOfMembers.getInstance().getListOfMembers());
		
		try{
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("data/serviceMembers.json")));
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String membersString = gson.toJson(membersJson);
			
			out.println(membersString);
			out.close();
		}catch(IOException exc){
			Controller.showErrorDialog(exc);
		}
	}
	
	private static JsonArray serializeMembers(List<Member> members){
		JsonArray membersArray = new JsonArray();
		
		for(int i = 0; i < members.size(); i++){
			Member member = members.get(i);
			
			JsonObject memberJson = new JsonObject();
			
			memberJson.addProperty("id", member.getId());
			memberJson.addProperty("name", member.getFirstName());
			memberJson.addProperty("lastName", member.getLastName());
			
			if(member.getBirthDate() != null){
				memberJson.addProperty("birthDate", member.getBirthDate().toString());
			}
			
			membersArray.add(memberJson);
		}
		
		return membersArray;
	}

	private static void messageToStatusBar(String message) {
		ParliamentAPIClientGUI instance = ParliamentAPIClientGUI.getInstance();
		instance.getTextArea().append(message + "\n");
	}

	public static void showAbouDialog() {
		JOptionPane.showMessageDialog(
				null,
				"Author: Marko Stevankovic",
				"About",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showErrorAddingNewMemberDialog() {
		JOptionPane.showMessageDialog(
				null,
				"Member has already been added!",
				"ERROR", 
				JOptionPane.ERROR_MESSAGE);		
	}
	


	private static void showErrorDialog(Exception exc) {
		JOptionPane.showMessageDialog(
				null,
				exc.getMessage(),
				"ERROR", 
				JOptionPane.ERROR_MESSAGE);
	}

}
