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
@Table(name = "linga_details_tab")
public class LingaDetails {

	@Id
	@GeneratedValue
	@Column(name= "linga_id")
	private Integer lid;
	
	@Column(name ="linga_idcard")
	private String lidcard;
	
	@Column(name ="linga_date_time")
	private String ldatetime;
	
	@Column(name ="linga_full_name")
	private String lfullname;

	@Column(name ="linga_phone_number")
	private String lphonenumber;
	
	@Column(name ="linga_aadher_number")
	private String laadharnumber;
	
	@Column(name ="linga_address")
	private String laddress;
	
	@Column(name="linga_code")
	private String lcode;
	
	@Column(name="linga_profile_code")
	private String lprofilecode;
	
	@Lob
	@Column(name = "linga_persons_imagedata") 
	private byte[] lpersonimagedata;
	
	@Column(name="linga_occupation")
	private String loccupation;
	
	@Column(name="linga_dateofmonth")
	private String ldateofmonth;
	
	@Column(name ="linga_family_name")
	private String lfamilyname;
	
}
