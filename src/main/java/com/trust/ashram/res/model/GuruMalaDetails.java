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
@Table(name = "gurumala_details_tab")
public class GuruMalaDetails {

	@Id
	@GeneratedValue
	@Column(name = "gurumala_id")
	private Integer gmid;

	@Column(name="gurumala_id_card")
	private String gmidcard;	
	
	@Column(name="gurumala_in_date")
	private String gmindate;		
		
	@Lob
	@Column(name = "gurumal_imagedata") 
	private byte[] gmimagedata;

	@Column(name="gurumala_full_name")			
	private String gmfullname;			

	@Column(name="gurumala_father_husband_name")
	private String gmfatherhusbandname;

	@Column(name="gurumala_date_of_birth")
	private String gmdateofbirth;		

	@Column(name="gurumala_age")	
	private String gmage;				

	@Column(name="gurumala_gender")				
	private String gmgender;				

	@Column(name="gurumala_occupation")			
	private String gmoccupation;			

	@Column(name="gurumala_present_address")
	private String gmpresentaddress;	

	@Column(name="gurumala_permanent_address")
	private String gmpermanentaddress;	

	@Column(name="gurumala_mobile_number")		
	private String gmmobilenumber;		

	@Column(name="gurumala_emergency_no")		
	private String gmemergencyno;	

	@Column(name="gurumala_code")
	private String gmcode;
	
	@Column(name="gurumala_profile_code")
	private String gmprofilecode;	
	
	@Column(name="gurumala_option")
	private String gmoption;
	
	@Column(name="gurumala_dateofmonth")
	private String gmdateofmonth;
	
	@Column(name="gurumala_disease")			
	private String gmdisease;	
}
