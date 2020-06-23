package com.sunny.admin.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity(name = "users")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 4352227673735365569L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private String userid;
	
	@Column(nullable = false , length = 50)
	private String firstname;
	
	@Column(nullable = false , length = 50)
	private String lastname;
	
	@Column(nullable = false , length = 50)
	private String email;
	
	@Column(nullable = false)
	private String encryptedpassword;
	

	private String emailverificationtoken;
	
	@Column(nullable = false)
	private Boolean emailverificationstatus = false;

	@Column(nullable = false)
	private Boolean isactive;
	
	@OneToMany(mappedBy = "userdetails" , cascade = CascadeType.ALL)
	private List<AddressEntity> addresses;                           // @JoinColumn(name="user_id")   hier "user_id" means
	                                                               // that only in DB we have a column with name "user_id"       
	                                                               // and in JAVA UserEntity class we have "id" field  
	                                                              // so now "id" field is mapped to DB "user_id"
	@ManyToMany(cascade = {CascadeType.PERSIST } , fetch = FetchType.EAGER )
	@JoinTable(name="users_roles" , joinColumns = @JoinColumn(name="user_id" , referencedColumnName = "id"),
			                        inverseJoinColumns = @JoinColumn(name="roles_id" , referencedColumnName = "id"))
	private Collection<RoleEntity> roles;
	
	
	
	
	public Collection<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RoleEntity> roles) {
		this.roles = roles;
	}

	public List<AddressEntity> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressEntity> addresses) {
		this.addresses = addresses;
	}

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


	
    
	
	
}
