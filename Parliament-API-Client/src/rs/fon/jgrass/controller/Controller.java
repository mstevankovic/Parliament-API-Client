package rs.fon.jgrass.controller;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
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
import rs.fon.jgrass.view.table_model.MemberTableModel;

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
		CollectionOfMembers instance = CollectionOfMembers.getInstance();
		instance.getListOfMembers().clear();
		
		JsonArray membersJson = Controller.loadJsonMembersFromFile("data/serviceMembers.json");
		
		instance.getListOfMembers().addAll(Controller.parseMembers(membersJson));
		
		Controller.refreshTable();
		
		Controller.messageToStatusBar("Table filled with data loaded from service.");
	}

	public static void updateMembers() {
		MemberTableModel model = (MemberTableModel)ParliamentAPIClientGUI.getInstance().getTable().getModel();
		if(model.getRowCount() <= 0){
			Controller.showErrorDialogSetValueAt("Table is empty...");
		} else {
			CollectionOfMembers instance = CollectionOfMembers.getInstance();
			instance.getListOfMembers().clear();
			instance.getListOfMembers().addAll(model.getTableMembers());
			try {
				Controller.saveChanges();
				Controller.messageToStatusBar("Updated fields saved.");
			} catch (Exception e) {
				Controller.showErrorDialog(e);
			}
		}
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
	
	private static List<Member> parseMembers(JsonArray membersJson) {
		List<Member> members = new ArrayList<>();
		
		for(int i = 0; i < membersJson.size(); i++){
			JsonObject memberJson = (JsonObject) membersJson.get(i);
			
			Member member = new Member();
			member.setId(memberJson.get("id").getAsInt());
			member.setFirstName(memberJson.get("name").getAsString());
			member.setLastName(memberJson.get("lastName").getAsString());
			
			if(member.getBirthDate() != null){
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
					member.setBirthDate(sdf.parse(memberJson.get("birthDate").getAsString()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			members.add(member);
		}
		
		return members;
	}

	private static JsonArray loadJsonMembersFromFile(String path) {
		try{
			FileReader in = new FileReader(path);
			Gson gson = new GsonBuilder().create();
			
			JsonArray membersJson = gson.fromJson(in, JsonArray.class);
			
			return membersJson;
			
		} catch(FileNotFoundException exc){
			Controller.showErrorDialog(exc);
		}
		return null;
	}

	private static void messageToStatusBar(String message) {
		ParliamentAPIClientGUI instance = ParliamentAPIClientGUI.getInstance();
		instance.getTextArea().append(message + "\n");
	}
	
	private static void refreshTable(){
		MemberTableModel model = (MemberTableModel)ParliamentAPIClientGUI.getInstance().getTable().getModel();
		model.fireTableDataChanged();
	}
	
	public static void saveChanges() throws Exception {
		JsonArray membersArray = new JsonArray();
		List<Member> members = CollectionOfMembers.getInstance().getListOfMembers();
		for (int i = 0; i < members.size(); i++) {
			
			Member m = members.get(i);

			JsonObject memberJson = new JsonObject();
			memberJson.addProperty("id", m.getId());
			memberJson.addProperty("name", m.getFirstName());
			memberJson.addProperty("lastName", m.getLastName());
			if (m.getBirthDate() != null)
				memberJson.addProperty("birthDate", new SimpleDateFormat("dd.MM.yyyy.").format(m.getBirthDate()));
			membersArray.add(memberJson);
		}

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("data/updatedMembers.json")));

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String membersString = gson.toJson(membersArray);

		out.println(membersString);
		out.close();
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

	public static void showErrorDialogSetValueAt(String message) {
		JOptionPane.showMessageDialog(
				null,
				message,
				"ERROR", 
				JOptionPane.ERROR_MESSAGE);
	}

}
