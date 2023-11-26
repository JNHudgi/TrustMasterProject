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
@Table(name = "volunteer_details_tab")
public class VolunteerDetails {

	@Id
	@GeneratedValue
	@Column(name = "volunteer_id")
	private Integer vid;

	@Column(name="volunteer_in_date")
	private String vindate;	

	@Column(name="volunteer_out_date")
	private String voutdate;	
	
	@Column(name="volunteer_id_card")
	private String vidcard;	

	@Column(name="volunteer_batch")
	private String vbatch;	

	@Column(name="volunteer_full_name")			
	private String vfullname;			

	@Column(name="volunteer_father_husband_name")
	private String vfatherhusbandname;

	@Column(name="volunteer_age")	
	private String vage;				

	@Column(name="volunteer_gender")				
	private String vgender;				

	@Column(name="volunteer_maritual_status")
	private String vmaritualstatus;	

	@Column(name="volunteer_occupation")			
	private String voccupation;			

	@Column(name="volunteer_permanent_address")
	private String vpermanentaddress;	

	@Column(name="volunteer_mobile_number")		
	private String vmobilenumber;		

	@Column(name="volunteer_emergencv_no")		
	private String vemergencvno;	

	@Lob
	@Column(name = "volunteer_imagedata") 
	private byte[] vimagedata;
	
	@Column(name="volunteer_code")
	private String vcode;
	
	@Column(name="volunteer_profile_code")
	private String vprofilecode;	
	
	
}
