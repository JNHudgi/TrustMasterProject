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
@Table(name = "event_details_tab")
public class EventDetails {

	@Id
	@GeneratedValue
	@Column(name= "event_id")
	private Integer eid;
	
	@Column(name ="event_idcard")
	private String eidcard;
	
	@Column(name ="event_date_time")
	private String edatetime;
	
	@Column(name ="event_name")
	private String ename;
	
	@Column(name ="event_venue")
	private String evenue;
	
	@Column(name ="event_person_names")
	private String epersonnames;
	
	@Column(name ="event_person_mobiles")
	private String epersonmobiles;
	
	@Column(name ="event_person_aadher")
	private String epersonaadhar;
	
	@Column(name ="event_person_address")
	private String epersonaddress;
	
	@Column(name="event_code")
	private String ecode;
	
	
	@Column(name="evnet_profile_code")
	private String eprofilecode;
	
	@Lob
	@Column(name = "evnet_persons_imagedata") 
	private byte[] epersonsimagedata;

	@Column(name="event_occupation")
	private String eoccupation;
	
	@Column(name="event_dateofmonth")
	private String edateofmonth;
	
}
