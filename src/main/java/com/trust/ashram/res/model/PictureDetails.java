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
@Table(name = "picture_details_tab")
public class PictureDetails {

	@Id
	@GeneratedValue
	@Column(name = "picture_id")
	private Integer pid;
	
	@Column(name="picture_number")				
	private String pnumber;			

	@Column(name="picture_name")
	private String pname;
	
	@Column(name="picture_description")
	private String pdescription;
	
	@Lob
	@Column(name = "picture_imagedata") 
	private byte[] pimagedata;
}
