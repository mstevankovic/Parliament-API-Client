package rs.fon.jgrass.view.table_model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import rs.fon.jgrass.controller.Controller;
import rs.fon.jgrass.domain.Member;
import rs.fon.jgrass.model.CollectionOfMembers;

public class MemberTableModel extends AbstractTableModel{
	
	private String[] columnNames = {"id", "First name", "Last name", "Birth date"};
	private List<Member> members;
	
	public MemberTableModel(){
		members = new ArrayList<>();
		members = CollectionOfMembers.getInstance().getListOfMembers();
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return members.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Member member = members.get(rowIndex);
		switch(columnIndex){
			case 0: return member.getId();
			case 1: return member.getFirstName();
			case 2: return member.getLastName();
			case 3:{
				if (member.getBirthDate() != null){
					return new SimpleDateFormat("dd.MM.yyyy.").format(member.getBirthDate()).toString();
				}
			}
			default: return "N/A";
		}
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		 if(!isCellEditable(rowIndex, columnIndex)){
	            return;
		 } else {
			 
			 Member member = members.get(rowIndex);
			 String value = aValue.toString();
			 
			 switch(columnIndex){
				 case 1: {
					 if(value == null || value.isEmpty()){
						 String message = "Bro...firstName must not be an empty nor null String!";
						 Controller.showErrorDialogSetValueAt(message);
						 return;
					 } else{
						 member.setFirstName(aValue.toString());
						 break;
					 }
				 }
				 case 2:{
					 if(value == null || value.isEmpty()){
						 String message = "Bro...lastName must not be an empty nor null String!";
						 Controller.showErrorDialogSetValueAt(message);
					 } else{
						 member.setLastName(aValue.toString());
						 break;
					 }
				 }
				 case 3: try {
					 	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
						member.setBirthDate(sdf.parse(aValue.toString()));
						break;
					} catch (ParseException e) {
						Controller.showErrorDialogSetValueAt(e.getMessage());
					}
				 }
		 }
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// return columnIndex != 1;
		return columnIndex == 1 ? false : true;
	}
	
	public List<Member> getTableMembers(){
		return members;
	}
}
