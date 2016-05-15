package rs.fon.jgrass.view.table_model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import rs.fon.jgrass.controller.Controller;
import rs.fon.jgrass.domain.Member;
import rs.fon.jgrass.util.ParliamentAPICommunication;

public class MemberTableModel extends AbstractTableModel{
	
	private String[] columnNames = {"id", "First name", "Last name", "Birth date"};
	private List<Member> members = new ArrayList<>();
	
	public MemberTableModel() {
		ParliamentAPICommunication instance = ParliamentAPICommunication.getInstance();
		members = instance.getListOfMembers();
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
		Member m = members.get(rowIndex);
		
		switch (columnIndex) {
			case 0: return m.getId();
			case 1: return m.getFirstName();
			case 2: return m.getLastName();
			case 3:
					if (m.getBirthDate() != null){
						return (new SimpleDateFormat("dd.MM.yyyy.").format(m.getBirthDate()));
					}
			default: return "N/A";
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex != 0;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Member m = members.get(rowIndex);
		String value = aValue.toString();
		switch(columnIndex){
			case 0: return;
			case 1: {
				if(value.isEmpty()){
					Controller.showErrorDialog("Bro...first name cannot be an empty string!");
				} else {
					m.setFirstName(value);
					break;
				}
			}
			case 2: {
				if(value.isEmpty()){
					Controller.showErrorDialog("Bro...last name cannot be an empty string!");
				} else {
					m.setLastName(value);
					break;
				}
			}
			case 3: {
				try{
					m.setBirthDate(new SimpleDateFormat("dd.MM.yyyy.").parse(value));
					break;
				}catch(ParseException exc){
					Controller.showErrorDialog(exc);
				}
			}
		}
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	public List<Member> getMembers() {
		return members;
	}
}
