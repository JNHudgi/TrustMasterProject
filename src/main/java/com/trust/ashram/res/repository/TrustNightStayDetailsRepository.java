package com.trust.ashram.res.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.trust.ashram.res.model.NightStayDetails;

public interface TrustNightStayDetailsRepository extends JpaRepository<NightStayDetails, Integer>{

	@Query("SELECT nsid,nsfullname,nsmobilenumber,nsdays,nsfulladdress,nsfamilyimagedata FROM NightStayDetails WHERE ROWNUM <= 10")
	List<Object[]> getAllNightShiftSpecficColList();
	
	@Query("SELECT nsid,nsidcard,nsfullname,nsmobilenumber,nsaadharnumber,nsdays FROM NightStayDetails v WHERE v.nsidcard like %:name% OR v.nsfullname like %:name% OR v.nsaadharnumber like %:name% OR v.nsmobilenumber like %:name%")
	List<Object[]> getOneOrAllNightStayDetails(String name);

	@Modifying
	@Query("UPDATE NightStayDetails v SET v.nsidcard=:nsidcard, v.nsfullname=:nsfullname, v.nsfamilynames=:nsfamilynames, v.nsaadharnumber=:nsaadharnumber,v.nsmobilenumber=:nsmobilenumber, v.nsalternetnumber=:nsalternetnumber,v.nsindate=:nsindate,v.nsoutdate=:nsoutdate,v.nsdays=:nsdays,v.nsfulladdress=:nsfulladdress,v.nscode=:nscode WHERE v.nsid=:nsid")
	Integer getUpdateNightStayOnlyDetails(String nsidcard, String nsfullname, String nsfamilynames, String nsaadharnumber,
			String nsmobilenumber, String nsalternetnumber, String nsindate, String nsoutdate, String nsdays,
			String nsfulladdress, String nscode,Integer nsid);
	
	@Modifying
	@Query("UPDATE NightStayDetails v SET v.nsfamilyimagedata=:nsfamilyimagedata WHERE v.nsid=:nsid")
	Integer getUpdateNightStayFamilyDetails(byte[] nsfamilyimagedata,Integer nsid);
	
	@Modifying
	@Query("UPDATE NightStayDetails v SET v.nsaadharimagedata=:nsaadharimagedata WHERE v.nsid=:nsid")
	Integer getUpdateNightStayAadharDetails(byte[] nsaadharimagedata,Integer nsid);

	@Query("SELECT v.nsid,v.nsfullname,v.nsmobilenumber,v.nsprofilecode,v.nsfulladdress FROM NightStayDetails v")
	List<Object[]> getDynamicNightStayDeatils();

	@Modifying
	@Query("UPDATE NightStayDetails v SET v.nscode=:nscode WHERE v.nsid=:nsid")
	Integer nsMoveToShivapur(Integer nsid, String nscode );

	@Query("SELECT v.nsid,v.nsfullname,v.nsmobilenumber,v.nsaadharnumber,v.nsprofilecode,v.nscode FROM NightStayDetails v WHERE v.nscode=:nscode")
	List<Object[]> getShivapurNightStayDeatils(String nscode);
	
	@Query("SELECT nsid,nsfullname,nsmobilenumber,nsaadharnumber,nsprofilecode,nsidcard FROM NightStayDetails v WHERE v.nsidcard like %:name% OR v.nsfullname like %:name% OR v.nsaadharnumber like %:name% OR v.nsmobilenumber like %:name%")
	List<Object[]> getDynamicSearchOptionForNightStayOneOrAll(String name);

}
