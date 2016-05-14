package rs.fon.jgrass.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Member {
	@SerializedName("id")
	private int id;
	
	@SerializedName ("name")
	private String firstName;
	
	@SerializedName("lastName")
	private String lastName;
	
	@SerializedName("birthDate")
	private Date birthDate;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
		return "Member [id: " + getId() + 
				", firstName: " + getFirstName() + 
				", lastName: " + getLastName() + 
				", birthday: " + sdf.format(getBirthDate())+
				"]";
	}
}
