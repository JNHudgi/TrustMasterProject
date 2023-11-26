package com.trust.ashram.res.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_details_tab")
public class AdminDetails {

	@Id
	@GeneratedValue
	@Column(name = "admin_id")
	private Integer aid;
	
	@Column(name = "admin_name")
	private String aname;
	
	@Column(name = "admin_password")
	private String apassword;

	@Column(name = "admin_visitied_date_time")
	private String avisitieddatetime;
	
	@Column(name = "admin_gender")
	private String agender;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "admin_details_role_tab",
			joinColumns = @JoinColumn(name="admin_id")
			)
	@Column(name = "admin_role")
	private Set<String> arole;
	
	@Column(name = "admin_email")
	private String aemail;
	
	@Column(name = "admin_phone")
	private String aphone;
	
	@Column(name = "admin_address")
	private String aaddress;
	
	@Lob
	@Column(name = "admin_image") 
	private byte[] aimage;
	
}
