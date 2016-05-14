package rs.fon.jgrass.view.table_model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import rs.fon.jgrass.domain.Member;
import rs.fon.jgrass.model.CollectionOfMembers;

public class MemberTableModel extends AbstractTableModel{
	
	private String[] columnNames = {"id", "First name", "Last name", "Birth date"};
	private List<Member> members;
	
	public MemberTableModel(){
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
		}
		return null;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

}
