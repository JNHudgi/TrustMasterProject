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
@Table(name = "yoga_details_tab")
public class YogaDetails {

	@Id
	@GeneratedValue
	@Column(name = "yoga_id")
	private Integer yid;

	@Column(name="yoga_in_date")
	private String yindate;	

	@Column(name="yoga_out_date")
	private String youtdate;	
		
	@Column(name="yoga_picture")
	private String ypicture;	

	@Column(name="yoga_full_name")			
	private String yfullname;			

	@Column(name="yoga_father_husband_name")
	private String yfatherhusbandname;

	@Column(name="yoga_date_of_birth")
	private String ydateofbirth;		

	@Column(name="yoga_age")	
	private String yage;				

	@Column(name="yoga_gender")				
	private String ygender;			
	
	@Column(name="yoga_nameopt")				
	private String ynameopt;

	@Column(name="yoga_maritual_status")
	private String ymaritualstatus;	

	@Column(name="yoga_blood_group")
	private String ybloodgroup;		

	@Column(name="yoga_nationality")		
	private String ynationality;		

	@Column(name="yoga_occupation")			
	private String yoccupation;			

	@Column(name="yoga_present_address")
	private String ypresentaddress;	

	@Column(name="yoga_permanent_address")
	private String ypermanentaddress;	

	@Column(name="yoga_disease")			
	private String ydisease;		

	@Column(name="yoga_aadhar_number")	
	private String yaadharnumber;		

	@Column(name="yoga_mobile_number")		
	private String ymobilenumber;		

	@Column(name="yoga_emergency_no")		
	private String yemergencyno;	

	@Column(name="yoga_weight")	
	private String yweight;	

	@Column(name="yoga_bp")				
	private String ybp;	

	@Column(name="yoga_pulse_rate")				
	private String ypulserate;			

	@Column(name="yoga_sugar")
	private String ysugar;
	
	@Lob
	@Column(name = "yoga_imagedata") 
	private byte[] yimagedata;
	
	@Lob
	@Column(name = "yoga_imagedataappji") 
	private byte[] yimagedataappaji;
	
	@Lob
	@Column(name = "yoga_imagedatalogo") 
	private byte[] yimagedatalogo;
	
	@Column(name="yoga_id_card")
	private String yidcard;	

	@Column(name="yoga_code")
	private String ycode;
	
	@Column(name="yoga_profile_code")
	private String yprofilecode;	

	@Column(name="yoga_batch")
	private String ybatch;	
	
	@Column(name="yoga_attendee_sign")
	private String yattendeesign;	
	
	@Column(name="yoga_guardian_sign")
	private String yguardiansign;	
	
	@Column(name="yoga_incharge_sign")
	private String yinchargesign;	
	
	@Column(name="yoga_dateofmonth")
	private String ydateofmonth;
}
