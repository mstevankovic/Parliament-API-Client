package rs.fon.jgrass.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import rs.fon.jgrass.controller.Controller;
import rs.fon.jgrass.domain.Member;

public class ParliamentAPICommunication {
	
	private static ParliamentAPICommunication instance;
	private static final String membersURL = "http://147.91.128.71:9090/parlament/api/members?limit=20";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
	
	private List<Member> members;

	public static ParliamentAPICommunication getInstance(){
		if(instance == null){
			instance = new ParliamentAPICommunication();
		}
		return instance;
	}
	
	public List<Member> getListOfMembers(){
		if(members == null){
			members = new ArrayList<>();
		}
		return members;
	}
	
	private String sendGet(String StringUrl)throws Exception{
		URL url = new URL(StringUrl);
		
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		connection.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
		boolean end = false;
		String response = "";
		
		while(!end){
			String line = in.readLine();
			
			if(line == null){
				end = true;
			} else {
				response += line;
			}
		}
		in.close();
		
		return response;
	}

	private List<Member> loadMembersFromService() throws Exception {
		String result = sendGet(membersURL);
		
		Gson gson = new GsonBuilder().create();
		
		JsonArray membersJson = gson.fromJson(result, JsonArray.class);
		
		List<Member> members = new LinkedList<>();
		
		for(int i = 0; i < membersJson.size(); i++){
			JsonObject memberJson = (JsonObject)membersJson.get(i);
			
			Member m = new Member();
			m.setId(memberJson.get("id").getAsInt());
			m.setFirstName(memberJson.get("name").getAsString());
			m.setLastName(memberJson.get("lastName").getAsString());
			
			if(memberJson.get("birthDate") != null){
				m.setBirthDate(sdf.parse(memberJson.get("birthDate").getAsString()));
			}
			
			members.add(m);
		}
		
		return members;
	}
	
	public static JsonArray serializeMembers(List<Member> members){
		JsonArray membersArray = new JsonArray();

		for (int i = 0; i < members.size(); i++) {
			Member m = members.get(i);

			JsonObject memberJson = new JsonObject();
			memberJson.addProperty("id", m.getId());
			memberJson.addProperty("firstName", m.getFirstName());
			memberJson.addProperty("lastName", m.getLastName());
			
			if(m.getBirthDate() != null){
				memberJson.addProperty("birthDate", sdf.format(m.getBirthDate()));
			}
			
			membersArray.add(memberJson);
		}

		return membersArray;
	}
	
	public static void saveMembersToFile(){
		
		JsonArray membersJson = null;
		try {
			membersJson = serializeMembers(instance.loadMembersFromService());
		} catch (Exception exc) {
			Controller.showErrorDialog(exc);
		}
		
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
	
	public void loadMembersFromFile(String path){
		this.members.clear();
		
		try {
			FileReader reader = new FileReader(path);
			
			Gson gson = new GsonBuilder().create();
			JsonArray membersJson = gson.fromJson(reader, JsonArray.class);

			for (int i = 0; i < membersJson.size(); i++) {
				JsonObject memberJson = (JsonObject) membersJson.get(i);

				Member m = new Member();
				m.setId(memberJson.get("id").getAsInt());
				m.setFirstName(memberJson.get("firstName").getAsString());
				m.setLastName(memberJson.get("lastName").getAsString());
			
				if (memberJson.get("birthDate") != null)
					try {
						m.setBirthDate(sdf.parse(memberJson.get("birthDate").getAsString()));
					} catch (ParseException exc) {
						Controller.showErrorDialog(exc);
					}

				this.members.add(m);
			}
		} catch (IOException exc) {
			Controller.showErrorDialog(exc);
		}
	}
}
