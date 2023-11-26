package com.trust.ashram.res.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.trust.ashram.res.model.VisitorDetails;

public interface TrustVisitorDetailsRepository extends JpaRepository<VisitorDetails, Integer> {
	
	@Query("SELECT v.vid,v.vfname,v.vphonenumber,v.voccupation,v.vdistcity FROM VisitorDetails v WHERE v.vfname like %:name% OR v.vfatherhusbandname like %:name%")
	List<Object[]> getOneVisitorDetails(String name);
	
	@Query("SELECT v.vid,v.vfname,v.vphonenumber,v.voccupation,v.vdistcity,v.vstate FROM VisitorDetails v WHERE v.voccupation like %:name%")
	List<Object[]> getVisitorListByOccpation(String name);
	
	@Query("SELECT v.vid,v.vfname,v.vphonenumber,v.voccupation,v.vdistcity,v.vstate FROM VisitorDetails v WHERE v.vvistedDateTime like %:name%")
	List<Object[]> getVisitorListByDate(String name);
	
	Optional <List<VisitorDetails>> findByVfname(String info);
	
	Optional <List<VisitorDetails>> findByVphonenumber(String info);
	
	Optional <List<VisitorDetails>> findByVaadher(String info);
	
	Optional <List<VisitorDetails>> findByVpannumber(String info);

	@Query("SELECT COUNT(vpannumber) FROM VisitorDetails WHERE vpannumber=:vpannumber")
	Integer getPanNumberCount(String vpannumber);

	@Query("SELECT COUNT(vaadher) FROM VisitorDetails WHERE vaadher=:vaadher")
	Integer getAadharNumberCount(String vaadher);
	
	@Query("SELECT COUNT(vphonenumber) FROM VisitorDetails WHERE vphonenumber=:vphonenumber")
	Integer getPhoneNumberCount(String vphonenumber);

	Optional<List<VisitorDetails>> findByVoccupation(String name);

	@Query("SELECT vid,vfname,vphonenumber,voccupation,vdistcity,vimagedata FROM VisitorDetails WHERE ROWNUM <= 10")
	List<Object[]> getAllVisitorSpecficColList();

	@Query("SELECT v.vid,v.vfname,v.vphonenumber,v.vprofilecode,v.vdistcity FROM VisitorDetails v")
	List<Object[]> getDynamicVisitorDeatils();

	@Modifying
	@Query("UPDATE VisitorDetails v SET v.vcode=:vcode WHERE v.vid=:vid")
	Integer visitorMoveToShivapur(Integer vid, String vcode);

	@Query("SELECT v.vid,v.vfname,v.vphonenumber,v.vaadher,v.vprofilecode,v.vcode FROM VisitorDetails v WHERE v.vcode=:vcode")
	List<Object[]> getShivapurVisitorDeatils(String vcode);
	
	@Query("SELECT v.vid,v.vfname,v.vphonenumber,v.vaadher,v.vprofilecode FROM VisitorDetails v WHERE v.vfname like %:name% OR v.vphonenumber like %:name% OR v.vaadher like %:name%")
	List<Object[]> getDynamicSearchOptionForVisitorOneOrAll(String name);

	@Modifying
	@Query("UPDATE VisitorDetails  SET vfname=:vfname, vfatherhusbandname=:vfatherhusbandname, vphonenumber=:vphonenumber, valternetnumber=:valternetnumber,vdistcity=:vdistcity,voccupation=:voccupation WHERE vid=:vid")
	Integer getUpdateVisitorOnlyDetails(String vfname, String vfatherhusbandname, String vphonenumber,String valternetnumber, String vdistcity, String voccupation, Integer vid);

	@Modifying
	@Query("UPDATE VisitorDetails SET vimagedata=:vimagedata WHERE vid=:vid")
	Integer getUpdateVisiImageDetails(byte[] vimagedata, Integer vid);

	@Query("SELECT vid,vfname,vphonenumber,voccupation,vdistcity,vimagedata FROM VisitorDetails WHERE vdateofmonth=:vdateofmonth")
	List<Object[]> getVisitorPDFMonthlyWise(String vdateofmonth);

	@Query("SELECT vid,vfname,vphonenumber,vdistcity,voccupation,vdateofmonth,vprofilecode,vimagedata FROM VisitorDetails")
	List<Object[]> getVisitorDatabaseBackUp();

		
}
