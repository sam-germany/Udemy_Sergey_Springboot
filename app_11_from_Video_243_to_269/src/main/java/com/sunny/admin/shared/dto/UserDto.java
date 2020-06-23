package com.sunny.admin.shared.dto;

import java.io.Serializable;
import java.util.List;

public class UserDto implements Serializable {

	private static final long serialVersionUID = 1521466878759585959L;

	
	private long id;
	private String userid;
	private String firstname;
	private String lastname;
	private String email;
	private String password;
	private String encryptedpassword;
	private String emailverificationtoken;
	private Boolean emailverificationstatus  = false;
	private Boolean isactive;
	private List<AddressDto> addresses;
	
	
	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEncryptedpassword() {
		return encryptedpassword;
	}
	public void setEncryptedpassword(String encryptedpassword) {
		this.encryptedpassword = encryptedpassword;
	}
	public String getEmailverificationtoken() {
		return emailverificationtoken;
	}
	public void setEmailverificationtoken(String emailverificationtoken) {
		this.emailverificationtoken = emailverificationtoken;
	}
	public Boolean getEmailverificationstatus() {
		return emailverificationstatus;
	}
	public void setEmailverificationstatus(Boolean emailverificationstatus) {
		this.emailverificationstatus = emailverificationstatus;
	}
	public Boolean getIsactive() {
		return isactive;
	}
	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}
 	
	public List<AddressDto> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<AddressDto> addresses) {
		this.addresses = addresses;
	}
	
	
	
	
}


