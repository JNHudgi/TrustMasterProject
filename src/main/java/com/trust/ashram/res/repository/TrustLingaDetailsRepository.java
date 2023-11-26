package com.trust.ashram.res.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.trust.ashram.res.model.LingaDetails;

public interface TrustLingaDetailsRepository extends JpaRepository<LingaDetails, Integer>{

	@Query("SELECT lid,ldatetime,lfullname,lphonenumber,loccupation,laddress,lpersonimagedata FROM LingaDetails WHERE ROWNUM <= 10")
	List<Object[]> getAllLingaDeatilsSpecficColList();

	@Query("SELECT lid,ldatetime,lfullname,lphonenumber,loccupation,laddress FROM LingaDetails WHERE lfullname like %:name%")
	List<Object[]> getOneOrAllLingaDetails(String name);

	@Modifying
	@Query("UPDATE LingaDetails v SET v.lpersonimagedata=:lpersonimagedata WHERE v.lid=:lid")
	Integer getUpdateLingaImageDetails(byte[] lpersonimagedata, Integer lid);

	@Modifying
	@Query("UPDATE LingaDetails v SET v.ldatetime=:ldatetime, v.lfullname=:lfullname, v.lfamilyname=:lfamilyname, v.lphonenumber=:lphonenumber ,v.loccupation=:loccupation ,v.laddress=:laddress WHERE v.lid=:lid")
	Integer getUpdateLingaDetails(String ldatetime, String lfullname, String lfamilyname, String lphonenumber,
			String loccupation, String laddress, Integer lid);

	@Query("SELECT lid,ldatetime,lfullname,lphonenumber,loccupation,laddress,lpersonimagedata FROM LingaDetails WHERE ldateofmonth=:ldateofmonth")
	List<Object[]> getLingaPDFMonthlyWise(String ldateofmonth);

	@Query("SELECT lid,ldatetime,lfullname,lfamilyname,lphonenumber,loccupation,laddress,lprofilecode,ldateofmonth,lpersonimagedata FROM LingaDetails")
	List<Object[]> getLingaDatabaseBackUp();

}
