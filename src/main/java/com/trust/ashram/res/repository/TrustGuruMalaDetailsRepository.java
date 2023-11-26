package com.trust.ashram.res.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.trust.ashram.res.model.GuruMalaDetails;

public interface TrustGuruMalaDetailsRepository extends JpaRepository<GuruMalaDetails, Integer> {

	@Query("SELECT gmid,gmfullname,gmmobilenumber,gmoccupation,gmpresentaddress,gmimagedata FROM GuruMalaDetails WHERE ROWNUM <= 10")
	List<Object[]> getAllGuruMalaList();

	@Modifying
	@Query("UPDATE GuruMalaDetails v SET v.gmimagedata=:gmimagedata WHERE v.gmid=:gmid ")
	Integer getUpdateGuruMalaImageDetails(byte[] gmimagedata, Integer gmid);

	@Modifying
	@Query("UPDATE GuruMalaDetails v SET v.gmindate=:gmindate, v.gmfullname=:gmfullname, v.gmfatherhusbandname=:gmfatherhusbandname, v.gmgender=:gmgender ,v.gmoccupation=:gmoccupation ,v.gmpresentaddress=:gmpresentaddress, v.gmmobilenumber=:gmmobilenumber, v.gmemergencyno=:gmemergencyno ,v.gmprofilecode=:gmprofilecode, v.gmdateofmonth=:gmdateofmonth,v.gmdisease=:gmdisease WHERE v.gmid=:gmid")
	Integer getUpdateGuruMalaDetails(String gmindate, String gmfullname, String gmfatherhusbandname, String gmgender, String gmoccupation, String gmpresentaddress, String gmmobilenumber, String gmemergencyno, String gmprofilecode, String gmdateofmonth, String gmdisease, Integer gmid);

	@Query("SELECT gmid,gmfullname,gmmobilenumber,gmoccupation,gmpresentaddress,gmimagedata FROM GuruMalaDetails WHERE gmdateofmonth=:gmdateofmonth")
	List<Object[]> getGuruMalaPDFMonthlyWise(String gmdateofmonth);

	@Query("SELECT gmid,gmfullname,gmmobilenumber,gmoccupation,gmpresentaddress FROM GuruMalaDetails WHERE gmfullname like %:gmfullname%")
	List<Object[]> getGuruMalaSearchByName(String gmfullname);

	@Query("SELECT gmid,gmindate,gmfullname,gmfatherhusbandname,gmgender,gmmobilenumber,gmemergencyno,gmoccupation,gmpresentaddress,gmdateofmonth,gmprofilecode,gmimagedata FROM GuruMalaDetails")
	List<Object[]> getGuruMalaDatabaseBackUp();
}


