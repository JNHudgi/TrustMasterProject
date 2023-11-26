package com.trust.ashram.res.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.trust.ashram.res.model.VolunteerDetails;

public interface TrustVolunteerDetailsRepository extends JpaRepository<VolunteerDetails, Integer> {

	
	@Query("SELECT vid,vidcard,vbatch,vfullname,vprofilecode,vmobilenumber,vpermanentaddress,vimagedata FROM VolunteerDetails WHERE ROWNUM <= 10")
	List<Object[]> getAllYogaVolunteerList();

	@Modifying
	@Query("UPDATE VolunteerDetails v SET v.vimagedata=:vimagedata WHERE v.vid=:vid ")
	Integer getUpdateYogaVolunterImageDetails(byte[] vimagedata, Integer vid);


	@Modifying
	@Query("UPDATE VolunteerDetails v SET v.vindate=:vindate, v.voutdate=:voutdate, v.vbatch=:vbatch, v.vfullname=:vfullname,v.vfatherhusbandname=:vfatherhusbandname,v.vage=:vage,v.vgender=:vgender,v.vmaritualstatus=:vmaritualstatus,v.voccupation=:voccupation,v.vpermanentaddress=:vpermanentaddress,v.vmobilenumber=:vmobilenumber,v.vemergencvno=:vemergencvno,v.vprofilecode=:vprofilecode, v.vidcard=:vidcard WHERE v.vid=:vid")
	Integer getUpdateYogaVolunteerDetails(String vindate, String voutdate, String vbatch, String vfullname,String vfatherhusbandname, String vage, String vgender, 
			String vmaritualstatus,String voccupation,String vpermanentaddress, String vmobilenumber, String vemergencvno, String vprofilecode, String vidcard,Integer vid);

}
