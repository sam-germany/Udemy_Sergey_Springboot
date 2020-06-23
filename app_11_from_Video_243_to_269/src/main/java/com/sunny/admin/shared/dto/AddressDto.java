package com.sunny.admin.shared.dto;

public class AddressDto {

	private long id;
	private String addressid;
	private String city;
	private String country;
	private String streetname;
	private String postalcode;
	private String type;
  
	private UserDto userdetails;                //<-- only this line is little confusing, the main point
	                                             // is that we have to set in AddressDto that for which
	                                             // User exactelly we are setting this address,
	                                             // as we want to search also by Address that for which
	public String getAddressid() {              // user this Address belongs to, so this is the reason
		return addressid;
	}

	public void setAddressid(String addressid) {
		this.addressid = addressid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStreetname() {
		return streetname;
	}

	public void setStreetname(String streetname) {
		this.streetname = streetname;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UserDto getUserdetails() {
		return userdetails;
	}

	public void setUserdetails(UserDto userdetails) {
		this.userdetails = userdetails;
	}
   
	
	
	
	
}
