package rs.fon.jgrass.model;

import java.util.ArrayList;
import java.util.List;

import rs.fon.jgrass.controller.Controller;
import rs.fon.jgrass.domain.Member;

public class CollectionOfMembers {
	private static CollectionOfMembers instance;
	private List<Member> members;
	
	public static CollectionOfMembers getInstance(){
		if(instance == null){
			instance = new CollectionOfMembers();
		}
		return instance;
	}
	
	private CollectionOfMembers(){
		members = new ArrayList<>();
	}
	
	public void addMember(Member member){
		if(!members.contains(member)){
			members.add(member);
		} else {
			Controller.showErrorAddingNewMemberDialog();
		}
	}
	
	public List<Member> getListOfMembers(){
		return members;
	}
}
