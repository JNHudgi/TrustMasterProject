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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "donation_details_tab")
public class DonationDetails {

	@Id
	@GeneratedValue
	@Column(name = "donation_id")
	private Integer did;

	@Column(name="donation_id_card")
	private String didcard;	
	
	@Column(name="donation_in_date")
	private String dindate;		
		
	@Lob
	@Column(name = "donation_imagedata") 
	private byte[] dimagedata;
	
	@Column(name="donation_items")
	private String ditems;

	@Column(name="donation_full_name")			
	private String dfullname;			

	@Column(name="donation_father_husband_name")
	private String dfatherhusbandname;

	@Column(name="donation_date_of_birth")
	private String ddateofbirth;		

	@Column(name="donation_age")	
	private String dage;				

	@Column(name="donation_gender")				
	private String dgender;				

	@Column(name="donation_occupation")			
	private String doccupation;			

	@Column(name="donation_present_address")
	private String dpresentaddress;	

	@Column(name="donation_permanent_address")
	private String dpermanentaddress;	

	@Column(name="donation_mobile_number")		
	private String dmobilenumber;		

	@Column(name="donation_emergency_no")		
	private String demergencyno;	

	@Column(name="donation_code")
	private String dcode;
	
	@Column(name="donation_profile_code")
	private String dprofilecode;	
	
	@Column(name="donation_option")
	private String doption;
	
	@Column(name="donation_dateofmonth")
	private String ddateofmonth;

}
