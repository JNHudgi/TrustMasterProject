package com.trust.ashram.res.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="NightStay_Details_Tab")
public class NightStayDetails {

	@Id
	@GeneratedValue
	@Column(name="NightStay_Id")
	private Integer nsid;
	
	@Column(name="nightstay_Idcard")			
	private String nsidcard;			
	
	@Column(name="nightstay_full_name")			
	private String nsfullname;			
	
	@Column(name="nightstay_family_names")			
	private String nsfamilynames;
	
	@Column(name="nightstay_aadhar_number")	
	private String nsaadharnumber;		

	@Column(name="nightstay_mobile_number")		
	private String nsmobilenumber;		

	@Column(name="nightstay_alternet_number")		
	private String nsalternetnumber;		

	@Column(name="nightstay_in_date")
	private String nsindate;	

	@Column(name="nightstay_out_date")
	private String nsoutdate;
	
	@Column(name="nightstay_days")
	private String nsdays;
	
	@Column(name="nightstay_full_address")
	private String nsfulladdress;

	@Column(name="nightstay_profile_code")
	private String nsprofilecode;	

	@Column(name="nightstay_code")
	private String nscode;
	
	@Lob
	@Column(name = "ns_family_imagedata") 
	private byte[] nsfamilyimagedata;
	
	@Lob
	@Column(name = "ns_aadhar_imagedata") 
	private byte[] nsaadharimagedata;
	
}
