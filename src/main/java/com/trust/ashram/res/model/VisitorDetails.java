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
@Table(name = "visitor_details_tab")
public class VisitorDetails {

	@Id
	@GeneratedValue
	@Column(name = "visitor_id")
	private Integer vid;
	
	@Column(name = "visitor_fname")
	private String vfname;

	@Column(name = "visitor_fatherhusbandname")
	private String vfatherhusbandname;
	
	@Column(name = "visitor_gender")
	private String vgender;
	
	@Column(name = "visitor_vphonenumber")
	private String vphonenumber;

	@Column(name = "visitor_valternetnumber")
	private String valternetnumber;

	@Column(name = "visitor_aadhar")
	private String vaadher;
	
	@Column(name = "visitor_pannumber")
	private String vpannumber;

	
	@Column(name = "visitor_occupation")
	private String voccupation;
	
	@Column(name = "visitor_vistedDateTime")
	private String vvistedDateTime;

	@Column(name = "visitor_visitpupose")
	private String vvisitpurpose;
	
	@Column(name = "visitor_visittime")
	private String vtime;
	
	@Column(name = "visitor_remark")
	private String vremark;

	
	@Column(name = "visitor_distcity")
	private String vdistcity;

	@Column(name = "visitor_state")
	private String vstate;

	@Column(name = "visitor_locality")
	private String vlocality;

	@Lob
	@Column(name = "visitor_imagedata") 
	private byte[] vimagedata;

	
	@Column(name="visitor_code")
	private String vcode;	
	
	@Column(name="visitor_profile_code")
	private String vprofilecode;	

	@Column(name="visitor_dateofmonth")
	private String vdateofmonth;

}
