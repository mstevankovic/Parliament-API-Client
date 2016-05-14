package rs.fon.jgrass.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import rs.fon.jgrass.domain.Member;

public class ParliamentAPICommunication {
	
	private static final String membersURL = "http://147.91.128.71:9090/parlament/api/members";
	private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
	
	private String sendGETRequest(String url_) throws Exception {
		
		URL url = new URL(url_);
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
	
	public List<Member> getListOfMembers() throws Exception {
		
		List<Member> members = new ArrayList<>();
		String result = sendGETRequest(membersURL);
		
		Gson gson = new GsonBuilder().create();
		
		JsonArray membersJson = gson.fromJson(result, JsonArray.class);
		
		for(int i = 0; i < membersJson.size(); i++){
			JsonObject memberJson = (JsonObject) membersJson.get(i);
			
			Member member = new Member();
			member.setId(memberJson.get("id").getAsInt());
			member.setFirstName(memberJson.get("name").getAsString());
			member.setLastName(memberJson.get("lastName").getAsString());
			
			if(memberJson.get("birthDate") != null){
				member.setBirthDate(format.parse(memberJson.get("birthDate").getAsString()));
			}
			
			members.add(member);
		}
		
		return members;
	}
}
